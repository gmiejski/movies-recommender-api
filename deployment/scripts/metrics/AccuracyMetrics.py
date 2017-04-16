import datetime
import os
from ansible.AnsibleRunner import AnsibleRunner
from metrics import formatter
from metrics.RMSEMetric import RMSEMetric


class AccuracyMetrics:
    def __init__(self, result_folder="/tmp/magisterka/metrics/accuracy/", similarity=0.1,
                 similarity_method="similarity"):
        self.fold_results = []
        self.result_folder = result_folder
        self.similarity = similarity
        self.similarity_method = similarity_method

    def run(self, testFilePath, dataset, fold):
        print("Running accuracy metrics for dataset {} and fold {}".format(dataset, fold))
        start = datetime.datetime.now().replace(microsecond=0)
        test_ratings_count = self.__total_ratings_to_predict(testFilePath)
        prepared_cypher = self.__prepare_metric_cypher(testFilePath)
        AnsibleRunner.runAccuracyMetricCypher(dataset, fold,
                                              prepared_cypher)  # TODO inject mean common ratings instead of magic number 18 in cypher

        results = self.read_results(dataset, fold)

        end = datetime.datetime.now().replace(microsecond=0)
        self.fold_results.append(PartialResult(
            RMSEMetric.calculate(results), test_ratings_count, len(results), (end-start).seconds)
        )
        print("Finished accuracy metrics for dataset {} and fold {}".format(dataset, fold))

    def finish(self, dataset):
        print("Finishing accuracy metrics for dataset {}".format(dataset))
        result = FinalResult(self.fold_results)
        self.save_result(dataset, result.rmse, result.total_time, result.ratings_found_percentage)
        return result.rmse

    def save_result(self, test_name, rmse, time, percentageOfRatingsFound):
        if not os.path.exists(self.result_folder + test_name):
            os.makedirs(self.result_folder + test_name)
        result_file_name = "/accuracy_method:{}_similarity:{}.log".format(self.similarity_method, self.similarity)
        result_path = "{}{}{}".format(self.result_folder, test_name, result_file_name)
        with open(result_path, mode="w") as result_file:
            result_file.write("Folds results = " + ",".join(map(lambda x: str(x), self.fold_results)) + '\n')
            result_file.write("Final RMSE = {}\n".format(rmse))
            result_file.write("Ratings found for movies: {0:.2f}%\n".format(percentageOfRatingsFound))
            result_file.write("Total time in seconds: {}\n".format(formatter.strfdelta(time, inputtype="s")))

    def __prepare_metric_cypher(self, testFilePath):
        prediction_cypher = "/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api/movies-recommender-service/src/main/resources/cypher/similarity_predicted_rating_for_metric.cypher"
        cypher_template = self.load_file(prediction_cypher)

        prefix = """LOAD CSV WITH HEADERS FROM 'file://{}' AS line FIELDTERMINATOR '\t'
WITH TOINT(line.user_id) as user, TOINT(line.movie_id) as movie, TOFLOAT(line.rating) as original_rating
        """.format(testFilePath)

        ready_cypher = prefix + cypher_template.replace("{userId}", "user") \
            .replace("{movieId}", "movie") \
            .replace("{similarity}", str(self.similarity)) \
            .replace("{similarity_method}", self.similarity_method)
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
    def __init__(self, rmse, test_ratings_count, ratings_predicted, time_in_seconds):
        self.rmse = rmse
        self.test_ratings_count = test_ratings_count
        self.ratings_predicted = ratings_predicted
        self.time_in_seconds = time_in_seconds

    def __str__(self):
        return "PartialResult(RMSE={}, ratings_predicted={}, time={})".format(self.rmse, self.ratings_predicted, self.time_in_seconds)

class FinalResult:

    def __init__(self, partial_results):
        self.rmse = sum(map(lambda x: x.rmse, partial_results))/ float(len(partial_results))
        self.total_time = sum(map(lambda x: x.time_in_seconds, partial_results))
        ratings_found = sum(map(lambda x: x.ratings_predicted, partial_results))
        ratings_to_find = sum(map(lambda x: x.test_ratings_count, partial_results))
        self.ratings_found_percentage = ratings_found / ratings_to_find * 100

if __name__ == "__main__":
    metrics = AccuracyMetrics(similarity=0.1)
    test_name = "ml-100k"
    metrics.run(
        "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_test_0",
        test_name, "ml-100k_train_0")
    metrics.finish(test_name)
