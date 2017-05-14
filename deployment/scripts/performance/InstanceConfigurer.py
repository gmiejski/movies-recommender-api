from ec2.EC2Client import EC2Client
from ec2.EC2Instances import EC2Instances
from ec2.EC2Waiter import EC2Waiter
from scripts.ansible.AnsibleRunner import AnsibleRunner


class InstanceConfigurer:
    def __init__(self):
        self.aws_client = EC2Client()

        self.neo4jInstances = EC2Instances()
        self.neo4jInstancesIds = []
        self.applicationInstances = EC2Instances()
        self.applicationInstancesIds = []
        self.testDriverInstancesIds = []
        self.testDriverInstances = EC2Instances()

    def load_existing_instances(self):
        instances = self.aws_client.getInstances().instances
        if len(instances) == 0:
            print("No running instances found")
            return
        self.__save_neo4j_instances(instances)
        self.__save_service_instances(instances)
        self.__save_test_driver_instances(instances)
        print("loaded Neo4j instances: {}".format(self.neo4jInstances.instances))
        print("loaded Service instances: {}".format(self.applicationInstances.instances))
        print("loaded test driver instances: {}".format(self.testDriverInstances.instances))

    def prepare_instances(self, config):
        self.createNeo4jInstances(config["neo4j"])
        self.createApplicationInstances(config["service"])
        self.createTestDriverInstances(config["test-driver"])

    def createNeo4jInstances(self, neo4j_config):
        if neo4j_config["count"] > 0 and len(self.neo4jInstancesIds) == 0:
            ids = self.createInstances(neo4j_config["instance-type"], neo4j_config["count"], "neo4j")
            self.neo4jInstancesIds = ids

    def createApplicationInstances(self, service_config):
        if service_config["count"] > 0 and len(self.applicationInstancesIds) == 0:
            ids = self.createInstances(service_config["instance-type"], service_config["count"], "service")
            self.applicationInstancesIds = ids

    def createInstances(self, instance_type, count, purpose):
        instances_ids = self.aws_client.createInstances(instance_type, count, purpose)
        return instances_ids

    def wait_for_instances(self):
        all_instances = self.__get_all_ids()
        EC2Waiter.waitForRunningState(all_instances)

        self.neo4jInstances = self.aws_client.getInstances(self.neo4jInstancesIds)
        self.applicationInstances = self.aws_client.getInstances(self.applicationInstancesIds)
        self.testDriverInstances = self.aws_client.getInstances(self.testDriverInstancesIds)

    def run_apps(self, dryRun=False):
        if not dryRun:
            self.runNeoOnInstances(self.neo4jInstances.ips())
            self.runServices(self.applicationInstances.ips(), self.neo4jInstances.private_ips()[0])
            self.prepareTestDriver(self.testDriverInstances.ips()[0], self.applicationInstances.private_ips())

    def runNeoOnInstances(self, ips):
        print("Running neo4j on nodes with ips: {}".format(str(ips)))
        for ip in ips:
            self.runNeoOnSingleInstance(ip)

    def runServices(self, nodes_ips, neo4j_node_ip):
        print("Running service on nodes with ips: {} and neo4j_node_ip: {}".format(str(nodes_ips), str(neo4j_node_ip)))
        AnsibleRunner.runApplication(nodes_ips, neo4j_node_ip)

    def prepareTestDriver(self, testDriverIp, service_nodes_ips):
        print("Preparing test driver node with ip: {} and service nodes ips: {}".format(str(testDriverIp),
                                                                                        str(service_nodes_ips)))
        AnsibleRunner.prepare_test_driver(testDriverIp, service_nodes_ips)

    def createTestDriverInstances(self, test_driver_config):
        if test_driver_config["count"] > 0 and len(self.testDriverInstancesIds) == 0:
            ids = self.createInstances(test_driver_config["instance-type"], test_driver_config["count"], "test-driver")
            self.testDriverInstancesIds = ids

    def runNeoOnSingleInstance(self, instanceIp):
        AnsibleRunner.remote_restart_neo4j(instanceIp, "ml-100k", True)

    def instances(self):
        return {"neo4j": self.neo4jInstances, "service": self.applicationInstances}

    def service_ips(self):
        return self.applicationInstances.ips()

    def killAllInstances(self):
        all_instances = self.__get_all_ids()
        self.aws_client.killAllInstances(all_instances)
        self.neo4jInstances = EC2Instances()
        self.neo4jInstancesIds = []
        self.applicationInstances = EC2Instances()
        self.applicationInstancesIds = []

    def __get_all_ids(self):
        return self.neo4jInstancesIds + self.applicationInstancesIds + self.testDriverInstancesIds

    def __save_neo4j_instances(self, instances):
        neo4j_instances = list(filter(lambda i: "purpose" in i.tags.keys() and i.tags["purpose"] == "neo4j", instances))
        self.neo4jInstancesIds = list(map(lambda x: x.instanceId, neo4j_instances))
        self.neo4jInstances = EC2Instances(neo4j_instances)

    def __save_service_instances(self, instances):
        service_instances = list(
            filter(lambda i: "purpose" in i.tags.keys() and i.tags["purpose"] == "service", instances))
        self.applicationInstancesIds = list(map(lambda x: x.instanceId, service_instances))
        self.applicationInstances = EC2Instances(service_instances)

    def __save_test_driver_instances(self, instances):
        test_driver_instances = list(
            filter(lambda i: "purpose" in i.tags.keys() and i.tags["purpose"] == "test-driver", instances))
        self.testDriverInstancesIds = list(map(lambda x: x.instanceId, test_driver_instances))
        self.testDriverInstances = EC2Instances(test_driver_instances)

    def test_driver_ip(self):
        return self.testDriverInstances.ips()[0]
