import os
import requests


class PrecisionRecallMetric():
    def __init__(self, result_folder="/tmp/magisterka/metrics/precisionAndRecall/"):
        self.result_folder = result_folder

    def run(self, testFilePath, test_name):
        print("Running precision metrics: " + test_name)
        requests.post('http://localhost:8080/metrics/precision',
                      json={'testFilePath': testFilePath, 'testName': test_name}, )

    def finish(self, test_name):
        print("Finishing accuracy metrics for " + test_name)
        response = requests.get('http://localhost:8080/metrics/accuracy/result')
        response_json = response.json()
        rmse = response_json["result"]
        time = response_json["timeInSeconds"]
        percentageOfRatingsFound = response_json["others"]["percentageOfFoundRatings"]
        print("Total RMSE = {}\nRatings found for movies: {}%\nTotal time in seconds: {}".format(rmse,
                                                                                                 percentageOfRatingsFound,
                                                                                                 time))
        self.save_result(test_name, rmse, time, percentageOfRatingsFound)
        return rmse

    def save_result(self, test_name, rmse, time, percentageOfRatingsFound):
        if not os.path.exists(self.result_folder + test_name):
            os.makedirs(self.result_folder + test_name)
        with open(self.result_folder + test_name + "/accuracy.log", mode="w") as result_file:
            result_file.write("Final precision = {}\n".format(rmse))
            result_file.write("Ratings found for movies: {0:.2f}%\n".format(percentageOfRatingsFound))
            result_file.write("Total time in seconds: {0:.2f}s\n".format(time))


if __name__ == "__main__":
    metrics = PrecisionRecallMetric()
    test_name = "testNameMote"
    metrics.run(
            "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_test_0",
            test_name)
    metrics.finish(test_name)
