from ansible.AnsibleRunner import AnsibleRunner

from ec2.EC2Client import EC2Client

client = EC2Client()

client.createNeo4jInstances()
client.createApplicationInstances()
client.wait_for_startup()

AnsibleRunner.start_performance_tests(client.application_ips())

client.killAllInstances()
