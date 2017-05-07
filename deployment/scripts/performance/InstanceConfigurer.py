import paramiko

from ec2.EC2Client import EC2Client
from ec2.EC2Waiter import EC2Waiter
from scripts.ansible.AnsibleRunner import AnsibleRunner


class InstanceConfigurer:
    def __init__(self):
        self.neo4jInstances = None
        self.neo4jInstancesIds = []
        self.applicationInstances = None
        self.applicationInstancesIds = []
        self.aws_client = EC2Client()

    def prepare_instances(self, config):
        self.createNeo4jInstances(config["neo4j"])
        self.createApplicationInstances(config["service"])

    def createNeo4jInstances(self, neo4j_config):
        ids = self.createInstances(neo4j_config["instance-type"], neo4j_config["count"])
        self.neo4jInstancesIds = ids

    def createApplicationInstances(self, app_config):
        ids = self.createInstances(app_config["instance-type"], app_config["count"])
        self.applicationInstancesIds = ids

    def createInstances(self, instance_type, count):
        instances_ids = self.aws_client.createInstances(instance_type, count)
        return instances_ids

    def wait_for_instances(self):
        all_instances = self.neo4jInstancesIds + self.applicationInstancesIds
        EC2Waiter.waitForRunningState(all_instances)

        self.neo4jInstances = self.aws_client.getInstances(self.neo4jInstancesIds)
        self.applicationInstances = self.aws_client.getInstances(self.applicationInstancesIds)

    def run_apps(self):
        self.runNeoOnInstances(self.neo4jInstances.ips())
        AnsibleRunner.runApplication(self.applicationInstances.ips(), self.neo4jInstances.ips()[0])

    def runNeoOnInstances(self, ips):
        for ip in ips:
            self.runNeoOnSingleInstance(ip)

    def runNeoOnSingleInstance(self, instanceIp):
        paramiko.util.log_to_file("ssh.log")
        client = paramiko.SSHClient()
        client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        client.connect(instanceIp, username='ec2-user',
                       key_filename='/Users/grzegorz.miejski/.ssh/movies-recommender-service.pem')

        stdin, stdout, stderr = client.exec_command(
            '/home/ec2-user/programming/neo4j-community-3.0.4/data/databases/neo4j-database.sh 100k.db')
        for line in stdout:
            print('... ' + line.strip('\n'))
        for err in stderr:
            print('... ' + err.strip('\n'))
        client.close()

    def instances(self):
        return {"neo4j": self.neo4jInstances, "service": self.applicationInstances}
