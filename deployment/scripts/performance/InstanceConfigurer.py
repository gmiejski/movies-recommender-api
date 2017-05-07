import paramiko

from ec2.EC2Instances import EC2Instances
from ec2.EC2Client import EC2Client
from ec2.EC2Waiter import EC2Waiter
from scripts.ansible.AnsibleRunner import AnsibleRunner


class InstanceConfigurer:
    def __init__(self):
        self.neo4jInstances = EC2Instances()
        self.neo4jInstancesIds = []
        self.applicationInstances = EC2Instances()
        self.applicationInstancesIds = []
        self.aws_client = EC2Client()

    def load_existing_instances(self):
        instances = self.aws_client.getInstances().instances
        self.__save_neo4j_instances(instances)
        self.__save_service_instances(instances)
        print("loaded Neo4j instances: {}".format(self.neo4jInstances.instances))
        print("loaded Service instances: {}".format(self.applicationInstances.instances))

    def prepare_instances(self, config):
        self.createNeo4jInstances(config["neo4j"])
        self.createApplicationInstances(config["service"])

    def createNeo4jInstances(self, neo4j_config):
        if neo4j_config["count"] > 0:
            ids = self.createInstances(neo4j_config["instance-type"], neo4j_config["count"], "neo4j")
            self.neo4jInstancesIds = ids

    def createApplicationInstances(self, service_config):
        if service_config["count"] > 0:
            ids = self.createInstances(service_config["instance-type"], service_config["count"], "service")
            self.applicationInstancesIds = ids

    def createInstances(self, instance_type, count, purpose):
        instances_ids = self.aws_client.createInstances(instance_type, count, purpose)
        return instances_ids

    def wait_for_instances(self):
        all_instances = self.neo4jInstancesIds + self.applicationInstancesIds
        EC2Waiter.waitForRunningState(all_instances)

        self.neo4jInstances = self.aws_client.getInstances(self.neo4jInstancesIds)
        self.applicationInstances = self.aws_client.getInstances(self.applicationInstancesIds)

    def run_apps(self):
        self.runNeoOnInstances(self.neo4jInstances.ips())
        self.runServices(self.applicationInstances.ips(), self.neo4jInstances.ips()[0])

    def runServices(self, nodes_ips, neo4j_node_ip):
        print("Running service on nodes with ips: {} and neo4j_node_ip: {}".format(str(nodes_ips), str(neo4j_node_ip)))
        AnsibleRunner.runApplication(nodes_ips, neo4j_node_ip)

    def runNeoOnInstances(self, ips):
        print("Running neo4j on nodes with ips: {}".format(str(ips)))
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

    def service_ips(self):
        return self.applicationInstances.ips()

    def killAllInstances(self):
        ids = self.neo4jInstancesIds + self.applicationInstancesIds
        self.aws_client.killAllInstances(ids)
        self.neo4jInstances = EC2Instances()
        self.neo4jInstancesIds = []
        self.applicationInstances = EC2Instances()
        self.applicationInstancesIds = []

    def __save_neo4j_instances(self, instances):
        neo4j_instances = list(filter(lambda i: "purpose" in i.tags.keys() and i.tags["purpose"] == "neo4j", instances))
        self.neo4jInstancesIds = list(map(lambda x: x.instanceId, neo4j_instances))
        self.neo4jInstances = EC2Instances(neo4j_instances)

    def __save_service_instances(self, instances):
        service_instances = list(
            filter(lambda i: "purpose" in i.tags.keys() and i.tags["purpose"] == "service", instances))
        self.applicationInstancesIds = list(map(lambda x: x.instanceId, service_instances))
        self.applicationInstances = EC2Instances(service_instances)
