import matplotlib.pyplot as plt

class MetricsPlotter:

    def __init__(self):
        self.colors = {
            "%user": "r",
            "%system": "b",
            "%iowait": 'g',
            "%idle": 'k'
        }
        pass

    def plot_CPU_metric(self, file_path):
        """SAR based metrics"""
        with open(file_path) as metrics_file:
            keys, metrics = self.__read_metrics(metrics_file)
            print(str(keys))
            print(str(metrics))
            f = self.__create_cpu_figure(keys, metrics)
            self.__save_plot(f)

    def __save_plot(self, f=None):
        f.savefig("cpu.png", bbox_inches='tight')

    def __read_metrics(self, metrics_file):
        metrics = metrics_file.readlines()

        metrics = list(filter(lambda x: "all" in x, metrics))
        metrics_split = list(map(lambda x: x.replace("    ","\t").split("\t"), metrics))
        keys = list(map(lambda x: x[0].replace(" ","").replace("PM", ""), metrics_split))
        user_cpu = self.__to_floats(list(map(lambda x: x[2], metrics_split)))
        system_cpu = self.__to_floats(list(map(lambda x: x[4], metrics_split)))
        iowait = self.__to_floats(list(map(lambda x: x[5], metrics_split)))
        idle = self.__to_floats(list(map(lambda x: x[7], metrics_split)))
        metrics = {
            "%user" : user_cpu,
            "%system": system_cpu,
            "%iowait": iowait,
            "%idle": idle
        }
        return keys, metrics

    def __to_floats(self, l):
        return list(map(lambda x: float(x.replace(' ', '')), l))

    def __create_cpu_figure(self, keys, metrics):
        f = plt.figure()
        plt.ylabel('CPU usage %')
        plt.xlabel('time')

        x = range(0,len(keys))
        for key, values in metrics.items():
            plt.xticks(x, keys, rotation=90)
            plt.plot(x, values, self.colors[key], label=key, linewidth=2.0)
        plt.legend(metrics.keys())
        plt.show()
        return f

if __name__ == "__main__":
    MetricsPlotter().plot_CPU_metric("/tmp/magisterka/os_metrics/cpu.log")