import boto3
import paramiko
from ec2.EC2Instances import EC2Instances
from ec2.EC2Waiter import EC2Waiter
from scripts.ansible.AnsibleRunner import AnsibleRunner


class EC2Client:
    def __init__(self):
        self.ec2client = boto3.client('ec2')
        self.ec2 = boto3.resource('ec2')
        self.neo4jInstances = []
        self.applicationInstances = []

    def createNeo4jInstances(self, count=1):
        ids = self.createInstances(count)
        EC2Waiter.waitForRunningState(ids)
        self.neo4jInstances = self.getInstances(ids)
        self.runNeoOnInstances(self.neo4jInstances.ips())

    def createInstances(self, count):
        response = self.ec2client.run_instances(
                DryRun=False,
                ImageId='ami-541bea3b',
                MinCount=count,
                MaxCount=count,
                KeyName='movies-recommender-service',
                SecurityGroups=[
                    'movies-recommender-service-sg',
                ],
                InstanceType='t2.micro',
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
        self.killInstances(self.neo4jInstances.ids())
        self.killInstances(self.applicationInstances.ids())
        self.neo4jInstances = []
        self.applicationInstances = []

    def killInstances(self, ids=[]):
        self.ec2client.terminate_instances(InstanceIds=ids)
        EC2Waiter.waitForTerminatedState(ids)

    def createApplicationInstances(self, count=1):
        ids = self.createInstances(count)
        EC2Waiter.waitForRunningState(ids)
        self.applicationInstances = self.getInstances(ids)
        AnsibleRunner.runApplication(self.applicationInstances.ips(), self.neo4jInstances.ips()[0])

    def application_ips(self):
        # return ["localhost"]
        return self.applicationInstances.ips()
