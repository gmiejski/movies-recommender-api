from ec2.EC2Client import EC2Client

# Let's use Amazon S3

# instances = client.describe_instances()
client = EC2Client()
client.createNeo4jInstances()
client.createApplicationInstances()
# client.runNeoOnInstances(['i-b80f4504'])
client.killInstances()

# for i in ec2.instances.all():
#     print(i)
