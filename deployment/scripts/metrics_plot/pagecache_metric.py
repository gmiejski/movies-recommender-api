from metrics_plot.metrics_result import MetricsResult


class PageCacheMetric:
    def __init__(self):
        self.metric_name = "pagecache"
        self.colors = {
            "majflt/s": "r",
            "pgsteal/s": "b",
            "%vmeff": 'g'
        }
        self.labels = {
            "x": 'time',
            'y_left': 'faults/steals per second',
            "y_right": 'page reclaim efficiency'
        }

        self.double_axis = {
            "left": ["majflt/s", "pgsteal/s"],
            "right": ["%vmeff"]
        }

    def read_metrics(self, metrics_dir):
        """SAR based metrics. Return MetricsResult object"""
        with open("{}/{}.log".format(metrics_dir, self.metric_name)) as metrics_file:
            keys, metrics = self.__read_metrics(metrics_file)
            return MetricsResult(self.metric_name, keys, metrics, self.colors, self.labels, self.double_axis)

    def __read_metrics(self, metrics_file):
        metrics = metrics_file.readlines()

        metrics = list(filter(lambda x: "pgpgin" not in x and "Linux" not in x, metrics))
        metrics = list(filter(lambda x: len(x) > 0, map(
            lambda y: y.replace('\n',''), metrics
        )))
        metrics_split = list(
            map(lambda x: list(filter(lambda p: len(p) > 0, x.replace("    ", "\t").split("\t"))), metrics))

        keys = list(map(lambda x: x[0].strip().replace("PM", ""), metrics_split))
        majflt = self.__to_floats(list(map(lambda x: x[4], metrics_split)))
        pgsteal = self.__to_floats(list(map(lambda x: x[-2], metrics_split)))
        vmeff = self.__to_floats(list(map(lambda x: x[-1], metrics_split)))
        metrics = {
            "majflt/s": majflt,
            "pgsteal/s": pgsteal,
            "%vmeff": vmeff
        }
        return keys, metrics

    def __to_floats(self, l):
        return list(map(lambda x: float(x.strip()), l))
