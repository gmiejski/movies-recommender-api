class EC2Instance:
    @staticmethod
    def fromJson(json):
        return EC2Instance(json['InstanceId'],
                           json['PublicIpAddress'],
                           EC2Instance.__to_tags(json['Tags']),
                           json['PrivateIpAddress'])

    def __init__(self, instanceId, publicIp, tags, privateIp):
        self.instanceId = instanceId
        self.publicIp = publicIp
        self.tags = tags
        self.privateIp = privateIp

    @staticmethod
    def __to_tags(tags_list):
        tags = {}
        for tag_pair in tags_list:
            tags[tag_pair["Key"]] = tag_pair["Value"]
        return tags

    def __repr__(self):
        return "EC2Instance(instanceId = {}, publicId = {}, privateIp = {}, tags = {})".format(self.instanceId,
                                                                                               self.publicIp,
                                                                                               self.privateIp,
                                                                                               str(self.tags))
