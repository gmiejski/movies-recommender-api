from ansible.AnsibleRunner import AnsibleRunner

from ec2.EC2Client import EC2Client

client = EC2Client()
# client.createNeo4jInstances()
# client.createApplicationInstances()

AnsibleRunner.start_tests(client.application_ips())

# client.killAllInstances()
