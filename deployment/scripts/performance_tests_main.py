from ansible.AnsibleRunner import AnsibleRunner

from ec2.EC2Client import EC2Client
from performance.InstanceConfigurer import InstanceConfigurer

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
instance_configurer.prepare_instances(config)
instance_configurer.wait_for_instances()
instance_configurer.run_apps()

client = EC2Client()

# client.createNeo4jInstances()
# client.createApplicationInstances()
client.wait_for_startup()

AnsibleRunner.start_performance_tests(client.application_ips())

client.killAllInstances()
