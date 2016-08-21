from ec2.EC2Client import EC2Client

client = EC2Client()
client.createNeo4jInstances()
client.createApplicationInstances()
client.killAllInstances()
