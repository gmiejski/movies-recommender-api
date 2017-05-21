import matplotlib.pyplot as plt

from metrics_plot.cpu_metric import CPUMetric
from metrics_plot.disk_metric import DiskUtilityMetric
from metrics_plot.pagecache_metric import PageCacheMetric


class MetricsPlotter:
    def __init__(self, metrics_dir):
        self.metrics_dir = metrics_dir
        self.keys_count = 10

    def plot(self, metric_result):
        metric_name = metric_result.metric_name
        keys = metric_result.keys
        metrics = metric_result.metrics
        colors = metric_result.colors
        labels = metric_result.labels
        double_axis = metric_result.double_axis
        if len(double_axis.keys()) == 0:
            f = self.__create_figure(keys, metrics, colors, labels)
        else:
            f = self.__create_double_axis_figure(keys, metrics, colors, labels, double_axis)
        self.__save_plot(metric_name, self.metrics_dir, f)

    def __save_plot(self, metric_name, metrics_dir, figure):
        figure.savefig("{}/{}.png".format(metrics_dir, metric_name), bbox_inches='tight')

    def __create_figure(self, keys, metrics, colors, labels):
        f = plt.figure()
        plt.ylabel(labels['y'])
        plt.xlabel('time')
        limited__x_keys = self.prepare_keys(keys)
        x = range(0, len(limited__x_keys))
        for key, values in metrics.items():
            plt.xticks(x, limited__x_keys, rotation=90)
            plt.plot(x, values, colors[key], label=key, linewidth=2.0)
        plt.legend(metrics.keys())
        return f

    def __create_double_axis_figure(self, keys, metrics, colors, labels, double_axis):
        fig, ax1 = plt.subplots()
        limited__x_keys = self.prepare_keys(keys)
        x = range(0, len(limited__x_keys))
        plt.xticks(x, limited__x_keys, rotation=90)
        for left in double_axis["left"]:
            m = metrics[left]
            ax1.plot(x, m, colors[left], label=left, linewidth=2.0)
            ax1.set_ylabel(labels["y_left"], color=colors[left])
            ax1.tick_params('y', colors=colors[left])

            # ax1.plot(limited__x_keys, s1, 'b-')
        ax1.set_xlabel('time')
        ax1.legend(loc=2)

        ax2 = ax1.twinx()
        for right in double_axis["right"]:
            m = metrics[right]
            ax2.plot(x, m, colors[right], label=right, linewidth=2.0)
            ax2.set_ylabel(labels["y_right"], color=colors[right])
            ax2.tick_params('y', color=colors[right])
        ax2.legend(loc=1)
        fig.tight_layout()
        return fig

    def prepare_keys(self, keys):
        result = []
        important_keys_index_diff = int(len(keys) / 10)
        for x in range(0, len(keys)):
            if x % important_keys_index_diff == 0:
                result.append(keys[x])
            else:
                result.append("")
        return result


if __name__ == "__main__":
    metrics_dir = "/tmp/magisterka/perf/os_metrics"
    # MetricsPlotter(metrics_dir).plot(CPUMetric().read_metrics(metrics_dir))
    # MetricsPlotter(metrics_dir).plot(DiskUtilityMetric().read_metrics(metrics_dir))
    MetricsPlotter(metrics_dir).plot(PageCacheMetric().read_metrics(metrics_dir))
