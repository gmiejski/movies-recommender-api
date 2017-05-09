from ansible.AnsibleRunner import AnsibleRunner

from ec2.EC2Client import EC2Client
from performance.InstanceConfigurer import InstanceConfigurer
from performance.InstanceStateChecker import InstanceStateChecker

config = {
    "neo4j": {
        "count": 1,
        "cluster": None,
        "instance-type": "t2.micro"
    },
    "service": {
        "count": 1,
        "instance-type": "t2.micro"
    }
}

instance_configurer = InstanceConfigurer()
instance_configurer.load_existing_instances()
instance_configurer.prepare_instances(config)
instance_configurer.wait_for_instances()
instance_configurer.run_apps(dryRun=True)
# instance_configurer.run_apps(dryRun=False)

service_checker = InstanceStateChecker(instance_configurer.service_ips())
service_checker.wait_for_services()

AnsibleRunner.start_performance_tests(instance_configurer.service_ips())

# instance_configurer.killAllInstances()


if __name__ == "__main__":
    instance_configurer = InstanceConfigurer()
    instance_configurer.load_existing_instances()