class Neo4jHAConf:
    def __init__(self, masterNode, slave_nodes):

        pass


class Neo4jHANode:
    def __init__(self, ec2Instance, label):
        self.ec2Instance = ec2Instance
        self.label = label
