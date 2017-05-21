import sys

from metrics_plot.plot_metrics import MetricsPlotter


def print_usage(scriptName):
    print("Usage: python " + scriptName + " path_to_logs ")

if __name__ == "__main__":
    print(sys.argv)
    args = sys.argv
    # args = ["name", "ml-100k", "0.3"]  # TODO
    if len(args) != 2:
        print_usage(args[0])
        exit(1)

    os_metrics_path = args[1]

    MetricsPlotter().plot_CPU_metric(os_metrics_path)