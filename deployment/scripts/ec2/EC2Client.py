import boto3
import paramiko
from ec2.EC2Instances import EC2Instances
from ec2.EC2Waiter import EC2Waiter
from scripts.ansible.AnsibleRunner import AnsibleRunner


class EC2Client:
    def __init__(self):
        self.ec2client = boto3.client('ec2')
        self.ec2 = boto3.resource('ec2')
        self.neo4jInstances = None
        self.neo4jInstancesIds = []
        self.applicationInstances = None
        self.applicationInstancesIds = []

    def wait_for_startup(self):
        AnsibleRunner.runApplication(self.applicationInstances.ips(), self.neo4jInstances.ips()[0])

    def createInstances(self, instance_type, count):
        response = self.ec2client.run_instances(
            DryRun=False,
            ImageId='ami-541bea3b',
            MinCount=count,
            MaxCount=count,
            KeyName='movies-recommender-service',
            SecurityGroups=[
                'movies-recommender-service-sg',
            ],
            InstanceType=instance_type,
        )

        instancesIds = list(map(lambda i: i['InstanceId'], response['Instances']))
        return instancesIds

    def getInstances(self, ids=[]):
        response = self.ec2client.describe_instances(
            InstanceIds=ids
        )
        return EC2Instances.fromJson(response)

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

    def killAllInstances(self):
        ids = self.neo4jInstances.ids() + self.applicationInstances.ids()
        self.ec2client.terminate_instances(InstanceIds=ids)
        EC2Waiter.waitForTerminatedState(ids)
        self.neo4jInstances = []
        self.applicationInstances = []

    def application_ips(self):
        return self.applicationInstances.ips()
