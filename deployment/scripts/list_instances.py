

def print_instances(instance_configurer):
    all_instances = instance_configurer.instances()
    print("**************************************************************************")
    for key, instances in all_instances.items():
        print("{} instances:".format(key))
        for x in instances.instances:
            print("Id: {}, publicIp: {}".format(x.instanceId, x.publicIp))
        print("--------------------------------------------------------------------------")
    print("**************************************************************************")



from performance.InstanceConfigurer import InstanceConfigurer

instance_configurer = InstanceConfigurer()
instance_configurer.load_existing_instances()


print_instances(instance_configurer)
