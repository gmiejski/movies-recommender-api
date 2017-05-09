import boto3


class EC2Waiter:
    ec2client = boto3.client('ec2')

    @staticmethod
    def waitForState(ids, state):
        waiter = EC2Waiter.ec2client.get_waiter(state)
        try:
            waiter.wait(
                    DryRun=False,
                    InstanceIds=ids,
            )
        except Exception as e:
            print("Error waiting for instances turn into state: " + state)
        print("instances: " + str(ids) + " turned into state: " + state)

    @staticmethod
    def waitForRunningState(ids):
        EC2Waiter.waitForState(ids, 'instance_status_ok')

    @staticmethod
    def waitForTerminatedState(ids):
        EC2Waiter.waitForState(ids, 'instance_terminated')
