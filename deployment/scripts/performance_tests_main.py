from performance.InstanceConfigurer import InstanceConfigurer
from performance.InstanceStateChecker import InstanceStateChecker

from ansible.AnsibleRunner import AnsibleRunner

config = {
    "neo4j": {
        "count": 1,
        "cluster": None,
        "instance-type": "t2.micro"
    },
    "service": {
        "count": 0,
        "instance-type": "t2.small"
    },
    "test-driver": {
        "count": 0,
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

warmup_config = {
    "max_users": 50,
    "wait_interval": 500,
    "run_time": 2
}

# AnsibleRunner.run_warmup_on_driver(instance_configurer.test_driver_ip(), "RatingsSimulation", warmup_config)
# AnsibleRunner.run_warmup_on_driver(instance_configurer.test_driver_ip(), "RecommendationsSimulation", warmup_config)


AnsibleRunner.start_collecting_metrics(instance_configurer.neo4jInstances.ips())


reco_config = {
    "max_users": 30,
    "wait_interval": 500,
    "run_time": 1
}

ratings_config = {
    "max_users": 150,
    "wait_interval": 50,
    "run_time": 1
}

AnsibleRunner.run_tests_on_driver(instance_configurer.test_driver_ip(), "RecommendationsSimulation", reco_config)
# AnsibleRunner.run_tests_on_driver(instance_configurer.test_driver_ip(), "RatingsSimulation", ratings_config)

AnsibleRunner.download_os_metrics(instance_configurer.neo4jInstances.instances[0].publicIp)

print("Reco -> Rq/s : {}".format(1000 / reco_config['wait_interval'] * reco_config['max_users']))
print("Ratings -> Rq/s : {}".format(1000 / ratings_config['wait_interval'] * ratings_config['max_users']))
