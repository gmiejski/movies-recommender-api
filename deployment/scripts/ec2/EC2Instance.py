class EC2Instance:
    @staticmethod
    def fromJson(json):
        return EC2Instance(json['InstanceId'], json['PublicIpAddress'], EC2Instance.__to_tags(json['Tags']))

    def __init__(self, instanceId, publicIp, tags):
        self.instanceId = instanceId
        self.publicIp = publicIp
        self.tags = tags

    @staticmethod
    def __to_tags(tags_list):
        tags = {}
        for tag_pair in tags_list:
            tags[tag_pair["Key"]] = tag_pair["Value"]
        return tags

    def __repr__(self):
        return "EC2Instance(instanceId = {}, publicId = {}, tags = {})".format(self.instanceId, self.publicIp, str(self.tags))