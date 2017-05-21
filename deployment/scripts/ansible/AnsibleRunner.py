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
            value = v if " " not in v else "'{}'".format(v)
            result += "{}={} ".format(k, value)
        # return '--extra-vars="' + result +'"'
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
    def run_tests_on_driver(testDriverIp, verbose=True):
        command = ['ansible-playbook', 'run-test-on-test-driver.yaml',
                   '-i', '{},'.format(testDriverIp), ]
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
    def download_os_metrics(nodeIp, verbose=True):
        command = ['ansible-playbook', 'download-os-metrics.yaml',
                   '-i', '{},'.format(nodeIp), ]
        if verbose:
            command.append('-vvv')
        process = subprocess.Popen(
            command,
            cwd=AnsibleRunner.ansible_home,
            stderr=subprocess.STDOUT,
            env=AnsibleRunner.__get_env())
        process.communicate()
        return