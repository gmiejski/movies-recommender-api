import os
import requests


class AccuracyMetrics():
    def __init__(self, result_folder="/tmp/magisterka/metrics/accuracy/"):
        self.result_folder = result_folder
        self.fold_results = []

    def runAccuracyMetrics(self, testFilePath, test_name):
        print("Running accuracy metrics: " + test_name)
        response = requests.post('http://localhost:8080/metrics/accuracy',
                                 json={'testFilePath': testFilePath, 'testName': test_name}, )
        response_json = response.json()
        self.fold_results.append(response_json)
        print("Fold result = " + str(response_json))

    def finish(self, test_name):
        print("Finishing accuracy metrics for " + test_name)
        rmse = sum(self.fold_results) / float(len(self.fold_results))
        print("Total RMSE = " + str(rmse))
        self.save_result(test_name, rmse)
        return rmse

    def save_result(self, test_name, rmse):
        if not os.path.exists(self.result_folder + test_name):
            os.makedirs(self.result_folder + test_name)
        with open(self.result_folder + test_name + "/accuracy.log", mode="w") as result_file:
            result_file.write("Folds results = " + ",".join(map(lambda x: str(x), self.fold_results)) + '\n')
            result_file.write("Final RMSE = " + str(rmse) + "\n")


if __name__ == "__main__":
    metrics = AccuracyMetrics()
    test_name = "testNameMote"
    metrics.runAccuracyMetrics(
        "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_test_0",
            test_name)
    metrics.finish(test_name)
