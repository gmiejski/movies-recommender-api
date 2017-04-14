import os

from metrics.RMSEMetric import RMSEMetric


class AccuracyMetrics:
    def __init__(self, result_folder="/tmp/magisterka/metrics/accuracy/"):
        self.result_folder = result_folder
        self.fold_results = []

    def run(self, testFilePath, dataset, fold):
        print("Running accuracy metrics for dataset {} and fold {}".format(dataset, fold))
        test_ratings_count = self.__total_ratings_to_predict(testFilePath)
        prepared_cypher = self.__prepare_metric_cypher(testFilePath)
        # AnsibleRunner.runAccuracyMetricCypher(dataset, fold, prepared_cypher)

        results = self.read_results(dataset, fold)

        self.fold_results.append(PartialResult(RMSEMetric.calculate(results), test_ratings_count, len(results)))
        print("Finished accuracy metrics for dataset {} and fold {}".format(dataset, fold))

    def finish(self, test_name):
        # print("Finishing accuracy metrics for " + test_name)
        # percentageOfRatingsFound = response_json["others"]["percentageOfFoundRatings"]
        # print("Total RMSE = {}\nRatings found for movies: {}%\nTotal time in seconds: {}".format(rmse,
        #                                                                                          percentageOfRatingsFound,
        # self.save_result(test_name, rmse, time, percentageOfRatingsFound)
        # return rmse
        return None

    def save_partial_result(self, test_name, rmse, time, percentageOfRatingsFound):
        if not os.path.exists(self.result_folder + test_name):
            os.makedirs(self.result_folder + test_name)
        with open(self.result_folder + test_name + "/accuracy.log", mode="w") as result_file:
            result_file.write("Folds results = " + ",".join(map(lambda x: str(x), self.fold_results)) + '\n')
            result_file.write("Final RMSE = {}\n".format(rmse))
            result_file.write("Ratings found for movies: {0:.2f}%\n".format(percentageOfRatingsFound))
            result_file.write("Total time in seconds: {0:.2f}s\n".format(time))

    def save_result(self, test_name, rmse, time, percentageOfRatingsFound):
        if not os.path.exists(self.result_folder + test_name):
            os.makedirs(self.result_folder + test_name)
        with open(self.result_folder + test_name + "/accuracy.log", mode="w") as result_file:
            result_file.write("Folds results = " + ",".join(map(lambda x: str(x), self.fold_results)) + '\n')
            result_file.write("Final RMSE = {}\n".format(rmse))
            result_file.write("Ratings found for movies: {0:.2f}%\n".format(percentageOfRatingsFound))
            result_file.write("Total time in seconds: {0:.2f}s\n".format(time))

    def __prepare_metric_cypher(self, testFilePath):
        prediction_cypher = "/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api/movies-recommender-service/src/main/resources/cypher/similarity_predicted_rating_for_metric.cypher"
        cypher_template = self.load_file(prediction_cypher)

        prefix = """LOAD CSV WITH HEADERS FROM 'file://{}' AS line FIELDTERMINATOR '\t'
WITH TOINT(line.user_id) as user, TOINT(line.movie_id) as movie, TOFLOAT(line.rating) as original_rating
        """.format(testFilePath)

        ready_cypher = prefix + cypher_template.replace("{userId}", "user").replace("{movieId}", "movie")
        return ready_cypher

    def load_file(self, prediction_cypher):
        with open(prediction_cypher) as c:
            cypher = c.readlines()
        return " ".join(cypher)

    def read_results(self, dataset, fold):
        with open("{}{}/{}".format(self.result_folder, dataset, fold)) as results:
            line = results.readlines()[1:]
            a = map(lambda x: x.replace('"', ''), line)
            b = map(self.__parse_result_line, a)
            return list(b)

    def __parse_result_line(self, line):
        split = line.split(',')
        return float(split[2]), float(split[3])

    def __total_ratings_to_predict(self, testFilePath):
        with open(testFilePath) as f:
            for i, l in enumerate(f):
                pass
        return i + 1

class PartialResult:
    def __init__(self, rmse, test_ratings_count, ratings_predicted):
        self.rmse = rmse
        self.test_ratings_count = test_ratings_count
        self.ratings_predicted = ratings_predicted


if __name__ == "__main__":
    metrics = AccuracyMetrics()
    test_name = "ml-100k"
    metrics.run(
        "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_test_0",
        test_name, "ml-100k_train_0")
    metrics.finish(test_name)
