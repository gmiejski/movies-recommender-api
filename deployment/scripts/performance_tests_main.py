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
        "count": 2,
        "instance-type": "t2.micro"
    },
    "test-driver": {
        "count": 1,
        "instance-type": "t2.micro"
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

AnsibleRunner.run_tests_on_driver(instance_configurer.test_driver_ip())