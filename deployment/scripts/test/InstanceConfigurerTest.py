import unittest

from moto import mock_ec2

from ec2.EC2Client import EC2Client
from performance.InstanceConfigurer import InstanceConfigurer


class InstanceConfigurerTest(unittest.TestCase):

    @mock_ec2
    def test_prepare_instances(self):
        config = {
            "neo4j": {
                "count": 1,
                "cluster": None,
                "instance-type": "t2.micro"
            },
            "service": {
                "count": 2,
                "instance-type": "t2.micro"
            }
        }
        instance_configurer = InstanceConfigurer()
        instance_configurer.prepare_instances(config)
        instance_configurer.wait_for_instances()

        instances = instance_configurer.instances()
        self.assertEqual(len(instances["neo4j"].instances), 1)
        self.assertEqual(len(instances["service"].instances), 2)

if __name__ == '__main__':
    unittest.main()