import time
from requests import get


class InstanceStateChecker:
    def __init__(self, service_ips):
        self.service_ips = service_ips

    def wait_for_services(self):
        responding = []
        not_responding = self.service_ips
        while True:
            for ip in not_responding:
                response = None
                try:
                    response = get("http://{}:8080/users/ids".format(ip), timeout=3)
                except Exception:
                    print("Timeout on {}".format(ip))
                if response is not None and response.status_code == 200:
                    responding.append(ip)
                    not_responding.remove(ip)
            self.print_status(responding, not_responding)

            if len(not_responding) == 0:
                break
            else:
                time.sleep(1)

    def print_status(self, responding, not_responding):
        print("###############################################################")
        print("Instance state checker report:")
        for i in responding:
            print("Responding: {}".format(i))
        for i in not_responding:
            print("Not responding: {}".format(i))
        print("###############################################################")
