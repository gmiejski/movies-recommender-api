from ec2.EC2Instance import EC2Instance


class EC2Instances():
    def __init__(self, instances=[]):
        self.instances = instances

    def ips(self):
        return list(map(lambda x: x.publicIp, self.instances))

    def ids(self):
        return list(map(lambda x: x.instanceId, self.instances))

    @staticmethod
    def fromJson(json):
        if len(json['Reservations']) == 0:
            return EC2Instances()
        result = []

        for reservation in json['Reservations']:
            instances = reservation['Instances']
            for instance in instances:
                result.append(EC2Instance.fromJson(instance))
        return EC2Instances(result)
        # return EC2Instances(
        #         instances=list(
        #                 map(lambda instance: EC2Instance.fromJson(instance), json['Reservations'][0]['Instances'])))


