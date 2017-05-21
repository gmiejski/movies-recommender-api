class MetricsResult:
    def __init__(self, metric_name, keys, metrics, colors, labels, double_axis={}):
        self.metric_name = metric_name
        self.keys = keys
        self.metrics = metrics
        self.colors = colors
        self.labels = labels
        self.double_axis = double_axis

