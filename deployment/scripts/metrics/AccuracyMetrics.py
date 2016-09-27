import requests


class AccuracyMetrics():
    def __init__(self, result_folder="/tmp/magisterka/metrics/accuracy"):
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
        return rmse


if __name__ == "__main__":
    AccuracyMetrics().runAccuracyMetrics(
        "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_test_0",
        "testNameMote")