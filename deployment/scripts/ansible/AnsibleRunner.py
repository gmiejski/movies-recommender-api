import subprocess


class AnsibleRunner:
    ansible_home = "/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api/deployment"
    application_home = "/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api"

    @staticmethod
    def runApplication(ips, neo4j_host):
        process = subprocess.Popen(['/usr/local/bin/ansible-playbook', 'install-application.yaml',
                                    '-i', AnsibleRunner.create_ips_argument(ips),
                                    '-vvv',
                                    '--extra-vars', AnsibleRunner.prepare_extra_variables(neo4j_host)],
                                   cwd=AnsibleRunner.ansible_home,
                                   stderr=subprocess.STDOUT)
        process.communicate()
        return

    @staticmethod
    def create_ips_argument(ips):
        return ",".join(ips) + ','

    @staticmethod
    def prepare_extra_variables(neo4j_host):
        return "neo4j_host=" + neo4j_host

    @staticmethod
    def start_tests(application_ips):
        host = 'http://' + application_ips[0] + ':8080'
        process = subprocess.Popen(['./gradlew', 'loadTest',
                                    '-Psimulation=org.miejski.movies.recommender.performance.RatingsSimulation',
                                    '-PapplicationUrl=' + host, ],
                                   cwd=AnsibleRunner.application_home,
                                   stderr=subprocess.STDOUT)
        process.communicate()
        return
