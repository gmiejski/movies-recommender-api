class EC2Instance():
    @staticmethod
    def fromJson(json):
        return EC2Instance(json['InstanceId'], json['PublicIpAddress'])

    def __init__(self, instanceId, publicIp):
        self.instanceId = instanceId
        self.publicIp = publicIp
