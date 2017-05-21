from metrics_plot.metrics_result import MetricsResult


class DiskUtilityMetric:
    def __init__(self):
        self.metric_name = "disk"
        self.colors = {
            "r/s": "r",
            "w/s": "b",
            "%util": 'k'
        }
        self.labels = {
            "x": 'time',
            'y_left': 'r/w per second',
            "y_right": 'disk utility %'
        }

        self.double_axis = {
            "left": ["r/s", "w/s"],
            "right": ["%util"]
        }

    def read_metrics(self, metrics_dir):
        """iostat based metrics. Return MetricsResult object"""
        with open("{}/{}.log".format(metrics_dir, self.metric_name)) as metrics_file:
            keys, metrics = self.__read_metrics(metrics_file)
            return MetricsResult(self.metric_name, keys, metrics, self.colors, self.labels, self.double_axis)

    def __parse_date(self, str):
        import time
        try:
            t = str.split(' ')[1]
            datetime_object = time.strptime(t, '%H:%M:%S')
            return t
        except Exception as e:
            return None

    def __read_metrics(self, metrics_file):
        keys = []
        metrics_results = {
            "r/s": [],
            "w/s": [],
            "%util": []
        }

        metrics = metrics_file.readlines()
        metrics = list(map(lambda x: x.replace('\n', '').replace('    ', '\t'), metrics))
        for index in range(0, len(metrics)):
            line = metrics[index]
            if "Linux" in line or len(line) == 0:
                continue
            log_time = self.__parse_date(line)
            if log_time is not None:
                keys.append(log_time)
                index += 1
                second_line = metrics[index]
                if "Device" not in second_line:
                    raise Exception()
                index+=1
                metric_line = metrics[index]
                r = list(filter(lambda x: len(x) > 0, metric_line.split('\t')))
                r = list(map(lambda x: x.strip(), r))
                metrics_results["r/s"].append(r[3])
                metrics_results["w/s"].append(r[4])
                metrics_results["%util"].append(self.__get_util(r[-1]))
        return keys, metrics_results

    def __to_floats(self, l):
        return list(map(lambda x: float(x.replace(' ', '')), l))

    def __get_util(self, param):
        """argument like: '0.89   0.89   0.16'"""
        return list(filter(lambda x: len(x) > 0, param.split(" ")))[-1]
