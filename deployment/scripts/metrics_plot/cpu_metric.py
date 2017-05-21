from metrics_plot.metrics_result import MetricsResult


class CPUMetric:
    def __init__(self):
        self.colors = {
            "%user": "r",
            "%system": "b",
            "%iowait": 'g',
            "%idle": 'k'
        }
        self.labels = {
            "x": 'time',
            "y": 'CPU usage %'
        }

    def read_metrics(self, metrics_dir):
        """SAR based metrics. Return MetricsResult object"""
        with open("{}/cpu.log".format(metrics_dir)) as metrics_file:
            keys, metrics = self.__read_metrics(metrics_file)
            return MetricsResult("cpu", keys, metrics, self.colors, self.labels)

    def __read_metrics(self, metrics_file):
        metrics = metrics_file.readlines()

        metrics = list(filter(lambda x: "all" in x, metrics))
        metrics_split = list(
            map(lambda x: list(filter(lambda p: len(p) > 0, x.replace("    ", "\t").split("\t"))), metrics))
        keys = list(map(lambda x: x[0].replace(" ", "").replace("PM", ""), metrics_split))
        user_cpu = self.__to_floats(list(map(lambda x: x[2], metrics_split)))
        system_cpu = self.__to_floats(list(map(lambda x: x[4], metrics_split)))
        iowait = self.__to_floats(list(map(lambda x: x[5], metrics_split)))
        idle = self.__to_floats(list(map(lambda x: x[7], metrics_split)))
        metrics = {
            "%user": user_cpu,
            "%system": system_cpu,
            "%iowait": iowait,
            "%idle": idle
        }
        return keys, metrics

    def __to_floats(self, l):
        return list(map(lambda x: float(x.replace(' ', '')), l))
