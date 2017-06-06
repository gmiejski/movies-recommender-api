import subprocess


class AnsibleRunner:
    ansible_home = "/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api/deployment"
    application_home = "/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api"

    @staticmethod
    def runApplication(ips, neo4j_host):
        process = subprocess.Popen(['ansible-playbook', 'install-application.yaml',
                                    '-i', AnsibleRunner.create_ips_argument(ips),
                                    '-vvv',
                                    '--extra-vars', AnsibleRunner.prepare_extra_variables(neo4j_host)],
                                   cwd=AnsibleRunner.ansible_home,
                                   stderr=subprocess.STDOUT,
                                   env=AnsibleRunner.__get_env())
        process.communicate()
        return

    @staticmethod
    def runLocalApplication():
        process = subprocess.Popen(['ansible-playbook', 'run-application-local.yaml',
                                    '-vvv'],
                                   # cwd=AnsibleRunner.ansible_home,
                                   stderr=subprocess.STDOUT,
                                   env=AnsibleRunner.__get_env())
        process.communicate()
        return

    @staticmethod
    def create_ips_argument(ips):
        return ",".join(ips) + ','

    @staticmethod
    def prepare_extra_variables(neo4j_host):
        return "neo4j_host=" + neo4j_host

    @staticmethod
    def start_performance_tests(application_ips):
        host = 'http://' + application_ips[0] + ':8080'
        process = subprocess.Popen(['./gradlew', 'loadTest',
                                    '-Psimulation=org.miejski.movies.recommender.performance.RecommendationsSimulation',
                                    '-PapplicationUrl=' + host,
                                    '-Pmin_similarity=0.0',
                                    '-Psimilarity_method=cosine',
                                    '-PneighboursCount=30'],
                                   cwd=AnsibleRunner.application_home,
                                   stderr=subprocess.STDOUT,
                                   env=AnsibleRunner.__get_env())
        process.communicate()
        return

    @staticmethod
    def killLocalApplication():
        process = subprocess.Popen(['ansible-playbook', 'kill-application-local.yaml',
                                    '-vvv'],
                                   cwd=AnsibleRunner.ansible_home,
                                   stderr=subprocess.STDOUT,
                                   env=AnsibleRunner.__get_env())
        process.communicate()
        return

    @staticmethod
    def restartLocalNeo4j(db_name, verbose=False):
        command = ['ansible-playbook', 'restart-neo4j.yaml', '--extra-vars', "neo4j_db_folder=" + db_name]
        if verbose:
            command.append('-vvv')
        process = subprocess.Popen(
            command,
            cwd=AnsibleRunner.ansible_home,
            stderr=subprocess.STDOUT,
            env=AnsibleRunner.__get_env())
        process.communicate()
        return

    @staticmethod
    def runAccuracyMetricCypher(dataset, fold_name, cypher, verbose=False):
        # cypher = "MATCH (p:Person) return p.user_id limit 10"
        command = ['ansible-playbook', 'neo4j-shell-cypher.yaml', '--extra-vars',
                   AnsibleRunner._to_extra_vars({"dataset": dataset, "result_file": fold_name, "cypher": cypher})]
        if verbose:
            command.append('-vvv')
        process = subprocess.Popen(
            command,
            cwd=AnsibleRunner.ansible_home,
            stderr=subprocess.STDOUT,
            env=AnsibleRunner.__get_env())
        process.communicate()
        code = process.returncode
        if code != 0:
            raise Exception("Return code greater than 0")
        return

    @staticmethod
    def __get_env():
        return {
            "PATH": "/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api/deployment/scripts/runner/bin:/Library/Frameworks/Python.framework/Versions/3.4/bin:/Users/grzegorz.miejski/programming/spark/spark-1.6.0-bin-hadoop2.6/bin:/Library/Frameworks/Python.framework/Versions/3.4/bin:/usr/local/go/bin/:/Users/grzegorz.miejski/programming/apache-cassandra-2.1.7/bin:/Users/grzegorz.miejski/programming/scala-2.11.4/bin:/Users/grzegorz.miejski/home/programs/apache-storm-0.9.4/bin:/usr/local/heroku/bin:/Users/grzegorz.miejski/home/programs/apache-storm-0.9.4/bin:/Users/grzegorz.miejski/home/maven/bin:/Users/grzegorz.miejski/home/mongodb/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Users/grzegorz.miejski/.fzf/bin:/usr/local/sbin:/Users/grzegorz.miejski/programming/drivers"}

    @staticmethod
    def _to_extra_vars(params):
        "returns prepared ansible extra args from dict"
        result = ""
        for k, v in params.items():
            v = str(v)
            value = v if " " not in v else "'{}'".format(v)
            result += "{}={} ".format(k, value)
        return result

    @staticmethod
    def remote_restart_neo4j(instanceIp, dataset, verbose=False):
        command = ['ansible-playbook', 'remote-restart-neo4j.yaml',
                   '-i', '{},'.format(instanceIp),
                   '--extra-vars', AnsibleRunner._to_extra_vars({"neo4j_db_folder": dataset})]
        if verbose:
            command.append('-vvv')
        process = subprocess.Popen(
            command,
            cwd=AnsibleRunner.ansible_home,
            stderr=subprocess.STDOUT,
            env=AnsibleRunner.__get_env())
        process.communicate()
        return

    @staticmethod
    def prepare_test_driver(testDriverIp, service_nodes_ips, verbose=True):
        ips = ";".join(service_nodes_ips)
        service_nodes = "'\'{}'\'".format(ips)

        command = ['ansible-playbook', 'prepare-test-driver.yaml',
                   '-i', '{},'.format(testDriverIp),
                   '--extra-vars', AnsibleRunner._to_extra_vars({"service_nodes_ips": service_nodes})]
        if verbose:
            command.append('-vvv')
        process = subprocess.Popen(
            command,
            cwd=AnsibleRunner.ansible_home,
            stderr=subprocess.STDOUT,
            env=AnsibleRunner.__get_env())
        process.communicate()
        return

    @staticmethod
    def run_tests_on_driver(testDriverIp, simulation_name, simulation_config, verbose=True):
        config = simulation_config.copy()
        config.update({"simulation_name": simulation_name})
        command = ['ansible-playbook', 'run-test-on-test-driver.yaml',
                   '-i', '{},'.format(testDriverIp),
                   '--extra-vars', AnsibleRunner._to_extra_vars(config)]
        if verbose:
            command.append('-vvv')
        process = subprocess.Popen(
            command,
            cwd=AnsibleRunner.ansible_home,
            stderr=subprocess.STDOUT,
            env=AnsibleRunner.__get_env())
        process.communicate()
        return

    @staticmethod
    def start_collecting_metrics(ips, verbose=True):
        if len(ips) == 0:
            raise Exception("Cannot start collecting metrics without instance IP specified!")
        command = ['ansible-playbook', 'collect-os-metrics.yaml',
                   '-i', AnsibleRunner.create_ips_argument(ips), ]
        if verbose:
            command.append('-vvv')
        process = subprocess.Popen(
            command,
            cwd=AnsibleRunner.ansible_home,
            stderr=subprocess.STDOUT,
            env=AnsibleRunner.__get_env())
        process.communicate()
        if process.returncode > 0:
            raise Exception("Error running command: {}".format(str(command)))
        return

    @staticmethod
    def download_os_metrics(neo4j_node_ips, verbose=True):
        for nodeIp in neo4j_node_ips:
            command = ['ansible-playbook', 'download-os-metrics.yaml',
                       '-i', '{},'.format(nodeIp),
                       '--extra-vars', AnsibleRunner._to_extra_vars({'os_metrics_folder': nodeIp})
                       ]
            if verbose:
                command.append('-vvv')
            process = subprocess.Popen(
                command,
                cwd=AnsibleRunner.ansible_home,
                stderr=subprocess.STDOUT,
                env=AnsibleRunner.__get_env())
            process.communicate()
        return

    @staticmethod
    def run_warmup_on_driver(testDriverIp, simulation_name, warmup_config, verbose=True):
        config = warmup_config.copy()
        config.update({"simulation_name": simulation_name})
        command = ['ansible-playbook', 'run-warmup-on-test-driver.yaml',
                   '-i', '{},'.format(testDriverIp),
                   '--extra-vars', AnsibleRunner._to_extra_vars(config)]
        if verbose:
            command.append('-vvv')
        process = subprocess.Popen(
            command,
            cwd=AnsibleRunner.ansible_home,
            stderr=subprocess.STDOUT,
            env=AnsibleRunner.__get_env())
        process.communicate()
        return

    @staticmethod
    def runApplicationWithHAproxy(nodes_ips, neo4j_nodes_ips, verbose=False):
        master = neo4j_nodes_ips[0]
        slaves = neo4j_nodes_ips[1:]
        slave_nodes_ips = "a".join(slaves)
        slave_nodes_ips = "'\'{}'\'".format(slave_nodes_ips)

        command = ['ansible-playbook', 'install-application-HA-Neo4j.yaml',
                   '-i', AnsibleRunner.create_ips_argument(nodes_ips),
                   '--extra-vars',
                   AnsibleRunner._to_extra_vars({'master_node_ip': master,
                                                 'slave_ips': slave_nodes_ips})]
        if verbose:
            command.append('-vvv')
        process = subprocess.Popen(
            command,
            cwd=AnsibleRunner.ansible_home,
            stderr=subprocess.STDOUT,
            env=AnsibleRunner.__get_env())
        process.communicate()
        return


    @staticmethod
    def getNeo4jHAInitialHostsPort():
        return "5001"

    @staticmethod
    def get_all_hosts_ips_string(all_hosts):
        return ",".join(list(map(lambda x: "{}:{}".format(x, AnsibleRunner.getNeo4jHAInitialHostsPort()), all_hosts)))

    @staticmethod
    def runNeo4jHAMaster(master_node_ip, dataset, slave_nodes_ips, verbose=False):
        service_id = 1
        all_hosts = [master_node_ip] + slave_nodes_ips
        all_nodes_ips = AnsibleRunner.get_all_hosts_ips_string(all_hosts)

        return AnsibleRunner.runNeo4jHANode(master_node_ip, all_nodes_ips, dataset, service_id, False, True)

    @staticmethod
    def runNeo4jHASlave(slave_ip, service_id, master_node_ip, dataset, slave_nodes_ips, ):
        all_hosts = [master_node_ip] + slave_nodes_ips
        all_nodes_ips = AnsibleRunner.get_all_hosts_ips_string(all_hosts)

        return AnsibleRunner.runNeo4jHANode(slave_ip, all_nodes_ips, dataset, service_id, True, True)

    @staticmethod
    def runNeo4jHANode(node_public_ip, all_nodes_ips_string, dataset, service_id, is_slave, verbose=False):

        command = ['ansible-playbook', 'remote-restart-neo4j-HA.yaml',
                   '-i', '{},'.format(node_public_ip),
                   '--extra-vars',
                   AnsibleRunner._to_extra_vars({'neo4j_db_folder': dataset,
                                                 'initial_hosts': all_nodes_ips_string,
                                                 'server_id': service_id,
                                                 'is_slave_only': is_slave})]
        if verbose:
            command.append('-vvv')
        process = subprocess.Popen(
            command,
            cwd=AnsibleRunner.ansible_home,
            stderr=subprocess.STDOUT,
            env=AnsibleRunner.__get_env())
        process.communicate()
        return
