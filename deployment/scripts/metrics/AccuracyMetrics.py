import requests


class AccuracyMetrics():
    def __init__(self):
        pass

    def runAccuracyMetrics(self, testFilePath, test_name):
        print("Running accuracy metrics")
        response = requests.post('http://localhost:8080/metrics/accuracy',
                     json={'testFilePath': testFilePath, 'testName': test_name}, )
        pass

    def finish(self):
        print("Finishing accuracy metrics")
        pass
        # http post -> run


if __name__ == "__main__" :
    AccuracyMetrics().runAccuracyMetrics("/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_test_0", "testNameMote")