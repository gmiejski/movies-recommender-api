from performance.InstanceConfigurer import InstanceConfigurer
from performance.InstanceStateChecker import InstanceStateChecker

from ansible.AnsibleRunner import AnsibleRunner

config = {
    "neo4j": {
        "count": 2,
        # "cluster": None,
        "cluster": "HA",
        # "cluster": "Casual",
        "instance-type": "t2.micro"
    },
    "service": {
        "count": 1,
        "instance-type": "t2.micro"
    },
    "test-driver": {
        "count": 1,
        "instance-type": "t2.small"
    }
}

instance_configurer = InstanceConfigurer()
instance_configurer.load_existing_instances()
instance_configurer.prepare_instances(config)
instance_configurer.wait_for_instances()
# instance_configurer.run_apps(dryRun=True)
instance_configurer.run_apps(dryRun=False)

service_checker = InstanceStateChecker(instance_configurer.service_ips())
service_checker.wait_for_services()

warmup_reco_config = {
    "max_users": 100,
    "wait_interval": 500,
    "run_time": 2
}

warmup_ratings_config = {
    "max_users": 500,
    "wait_interval": 50,
    "run_time": 2
}

# AnsibleRunner.run_warmup_on_driver(instance_configurer.test_driver_ip(), "RatingsSimulation", warmup_ratings_config)
# AnsibleRunner.run_warmup_on_driver(instance_configurer.test_driver_ip(), "RecommendationsSimulation", warmup_reco_config)


AnsibleRunner.start_collecting_metrics(instance_configurer.neo4jInstances.ips())


reco_config = {
    "max_users": 200,
    "wait_interval": 1000,
    "run_time": 1
}

ratings_config = {
    "max_users": 10,
    "wait_interval": 50,
    "run_time": 1
}

# AnsibleRunner.run_tests_on_driver(instance_configurer.test_driver_ip(), "RecommendationsSimulation", reco_config)
AnsibleRunner.run_tests_on_driver(instance_configurer.test_driver_ip(), "RatingsSimulation", ratings_config)

AnsibleRunner.download_os_metrics(instance_configurer.neo4jInstances.ips())

print("Reco -> Rq/s : {}".format(1000 / reco_config['wait_interval'] * reco_config['max_users']))
print("Ratings -> Rq/s : {}".format(1000 / ratings_config['wait_interval'] * ratings_config['max_users']))
