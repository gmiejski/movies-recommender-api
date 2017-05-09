import boto3
import paramiko
from ec2.EC2Instances import EC2Instances
from ec2.EC2Waiter import EC2Waiter
from scripts.ansible.AnsibleRunner import AnsibleRunner


class EC2Client:
    def __init__(self):
        self.ec2client = boto3.client('ec2')
        self.ec2 = boto3.resource('ec2')

    def createInstances(self, instance_type, count, purpose):
        response = self.ec2client.run_instances(
            DryRun=False,
            ImageId='ami-b8fe20d7',
            MinCount=count,
            MaxCount=count,
            KeyName='movies-recommender-service',
            SecurityGroups=[
                'movies-recommender-service-sg',
            ],
            InstanceType=instance_type,
            TagSpecifications=[
                {
                    'ResourceType': 'instance',
                    'Tags': [
                        {
                            'Key': 'purpose',
                            'Value': purpose
                        },
                    ]
                },
            ]
        )

        instancesIds = list(map(lambda i: i['InstanceId'], response['Instances']))
        return instancesIds

    def getInstances(self, ids=[]):
        response = self.ec2client.describe_instances(
            InstanceIds=ids,
            Filters=[
                {
                    'Name': 'instance-state-name',
                    'Values': [
                        'pending', 'running'
                    ]
                },
            ],
        )
        return EC2Instances.fromJson(response)

    def killAllInstances(self, ids=[]):
        self.ec2client.terminate_instances(InstanceIds=ids)
        EC2Waiter.waitForTerminatedState(ids)
