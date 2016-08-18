import boto3


class EC2Client:
    def __init__(self):
        self.client = boto3.client('ec2')
        self.ec2 = boto3.resource('ec2')

    def createNeo4jInstances(self, number=1):
        ids = self.createInstances()
        self.waitForRunningState(ids)
        self.runNeoOnInstances(ids)
        pass

    def createInstances(self):
        response = self.client.run_instances(
                DryRun=False,
                ImageId='ami-db59afb4',
                MinCount=1,
                MaxCount=1,
                KeyName='movies-recommender-service',
                SecurityGroups=[
                    'movies-recommender-service-sg',
                ],
                InstanceType='t2.micro',
        )

        instancesIds = list(map(lambda i: i['InstanceId'], response['Instances']))
        return instancesIds

    def waitForRunningState(self, ids):
        waiter = self.client.get_waiter('instance_running')
        try:
            waiter.wait(
                    DryRun=False,
                    InstanceIds=ids,
                    # Filters=[
                    #     {
                    #         'Name': 'string',
                    #         'Values': [
                    #             'string',
                    #         ]
                    #     },
                    # ],
                    # NextToken='string',
                    MaxResults=123
            )
        except Exception:
            print("Error waiting for instances becomes running")

    def runNeoOnInstances(self, ids=[]):
        # response = self.client.describe_instances(
        #         InstanceIds=ids
        # )

        import paramiko
        paramiko.util.log_to_file("filename.log")

        client = paramiko.SSHClient()
        client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        client.connect('52.58.209.190', username='ec2-user',
                       key_filename='/Users/grzegorz.miejski/.ssh/movies-recommender-service.pem')

        stdin, stdout, stderr = client.exec_command(
            '/home/ec2-user/programming/neo4j-community-3.0.4/data/databases/neo4j-database.sh empty.db')
        for line in stdout:
            print('... ' + line.strip('\n'))
        for err in stderr:
            print('... ' + err.strip('\n'))
        client.close()

        # call('ssh -i /Users/grzegorz.miejski/.ssh/movies-recommender-service.pem ec2-user@52.58.209.190 /home/ec2-user/programming/neo4j-community-3.0.4/data/databases/neo4j-database.sh 100k.db')
        pass
