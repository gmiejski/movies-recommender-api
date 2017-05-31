from performance.InstanceConfigurer import InstanceConfigurer

instance_configurer = InstanceConfigurer()
instance_configurer.load_existing_instances()
instance_configurer.killAllInstances()
