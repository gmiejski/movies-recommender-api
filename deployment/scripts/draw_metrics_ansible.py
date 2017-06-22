import sys
import os

from metrics_plot.cpu_metric import CPUMetric
from metrics_plot.disk_metric import DiskUtilityMetric
from metrics_plot.pagecache_metric import PageCacheMetric
from metrics_plot.plot_metrics import MetricsPlotter
from shutil import copyfile


def print_usage(scriptName):
    print("Usage: python " + scriptName + " path_to_logs [os_metrics_result_path]")


def get_latest_sim_folder(files_list):
    simulations = list(
        filter(lambda x: "simulation" in x, files_list)
    )

    sim_timestamps = list(map(lambda x: int(x.split("-")[-1]), simulations))
    sim_timestamps.sort()
    latest_timestamp = sim_timestamps[-1]

    return list(filter(lambda x: str(latest_timestamp) in x, files_list))[0]


def find_png_files(os_metrics_path):
    files = os.listdir(os_metrics_path)

    png_files_names = list(filter(lambda x: "png" in x, files))
    return png_files_names


def copy_plots_to_last_reco_result(simulations_dir, os_metrics_path, folder_to_save_plots=None):
    path = simulations_dir
    name_list = os.listdir(path)
    lastest_file_name = get_latest_sim_folder(name_list)
    target_simulation_dir = "{}/{}".format(simulations_dir, lastest_file_name)

    png_files_paths = find_png_files(os_metrics_path)
    for png_file in png_files_paths:
        png_file_path = "{}/{}".format(os_metrics_path, png_file)
        if folder_to_save_plots is not None:
            png_target_file_path = "{}/{}/{}".format(target_simulation_dir, folder_to_save_plots, png_file )
            png_target_file_path_folder = "{}/{}".format(target_simulation_dir, folder_to_save_plots )
            if not os.path.exists(png_target_file_path_folder):
                print("Creating folder : {}".format(png_target_file_path_folder))
                os.makedirs(png_target_file_path_folder)
        else:
            png_target_file_path = "{}/{}".format(target_simulation_dir, png_file)
        copyfile(png_file_path, png_target_file_path)


if __name__ == "__main__":
    print(sys.argv)
    args = sys.argv
    os_metrics_path = "/Users/grzegorz.miejski/magisterka/perf/os_metrics"
    folder_to_save_plots = None

    if len(args) == 2:
        os_metrics_path = args[1]
    if len(args) == 3:
        os_metrics_path = args[1]
        folder_to_save_plots = args[2]

    metrics_plotter = MetricsPlotter(os_metrics_path)
    # metrics = [CPUMetric(), DiskUtilityMetric(), PageCacheMetric()]
    metrics = [CPUMetric(), DiskUtilityMetric()]

    for metric in metrics:
        metric_result = metric.read_metrics(os_metrics_path)
        metrics_plotter.plot(metric_result)

    copy_plots_to_last_reco_result("/Users/grzegorz.miejski/magisterka/perf", os_metrics_path, folder_to_save_plots)
