import os

from ansible.AnsibleRunner import AnsibleRunner
from metrics.AccuracyMetricsRunner import AccuracyMetricsRunner
from metrics.acc_metrics_details.average_rating_details import AverageRatingBasedDetails
from metrics.acc_metrics_details.n_best_neighbours_details import NeighboursCountBasedDetails
from metrics.acc_metrics_details.similarity_based_details import SimilarityBasedDetails
from neo4j_state.assertions.average_rating_assertion import AverageRatingAssertion
from neo4j_state.assertions.cosine_similarity_assertion import CosineSimilarityAssertion
from neo4j_state.assertions.movie_index_assertion import MovieIndexAssertion
from neo4j_state.assertions.movies_in_common_count import MoviesInCommonAssertion
from neo4j_state.assertions.neo4j_data_assertion import DataLoadedAssertion
from neo4j_state.assertions.pearson_similarity_assertion import PearsonSimilarityAssertion
from neo4j_state.assertions.pearson_similarity_with_sw import PearsonWithSWAssertion
from neo4j_state.assertions.person_index_assertion import PersonIndexAssertion
from neo4j_state.neo4j_cypher_executor import Neo4jCypherExecutor
from neo4j_state.neo4j_state import Neo4jStateAssertions


class LocalAccuracyMetricsRunner:
    def __init__(self, metrics_runners):
        self.metrics_runners = metrics_runners

    def find_cross_validation_folds_datas(self, folds_folder):
        print(folds_folder)
        files = os.listdir(folds_folder)
        train_file_names = list(filter(lambda x: "train" in x, files))
        test_file_names = list(filter(lambda x: "test" in x, files))
        number_of_folds = len(test_file_names)
        cross_validation_folds = []
        for x in range(0, number_of_folds):
            cross_validation_folds.append({
                "test": folds_folder + test_file_names[x],
                "train": folds_folder + train_file_names[x],
                "fold": train_file_names[x]})
        return cross_validation_folds

    def neo4j_assertions(self, fold_data):
        return [MovieIndexAssertion(), PersonIndexAssertion(), DataLoadedAssertion(fold_data["train"]),
                AverageRatingAssertion(), PearsonSimilarityAssertion(),
                MoviesInCommonAssertion(fold_data["train"], fold_data["fold"]),
                PearsonWithSWAssertion(fold_data["train"], fold_data["fold"], rerun=False),
                CosineSimilarityAssertion()
                ]

    def play(self, folds_directory, dataset):
        crossValidationDatas = self.find_cross_validation_folds_datas(folds_directory)

        for fold_data in crossValidationDatas:
            print("Start working on fold_data: {}".format(fold_data))
            AnsibleRunner.restartLocalNeo4j(fold_data["fold"])
            Neo4jStateAssertions(Neo4jCypherExecutor(), self.neo4j_assertions(fold_data)).run_assertions()

            for metric in self.metrics_runners:
                metric.run(fold_data["test"], dataset, fold_data["fold"])
        for metric in self.metrics_runners:
            metric.finish(dataset)


def generate_metrics_to_run():
    return [SimilarityBasedDetails(0.1, "similarity")]
    # return [NeighboursCountBasedDetails(neighbours_min_similarity=0.3, similarity_method="cosine", top_n_neighbours=10)]
    # return [SimilarityBasedDetails(0.05, "cosine"), NeighboursCountBasedDetails(0.0, "similarity", 20)]
    result_details = []
    # similarities = [ -1.0, -0.5, -0.3, 0.0, 0.05, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9]
    similarities = [0.0, 0.05, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9]
    similarity_methods = ["similarity", "pearson_with_sw", "cosine"]
    top_n = [10, 18, 30, 100]
    for s in similarities:
        for m in similarity_methods:
            # result_details.append(SimilarityBasedDetails(s, m))
            for n in top_n:
                result_details.append(NeighboursCountBasedDetails(s, m, n))
    result_details.append(AverageRatingBasedDetails())
    return result_details


if __name__ == "__main__":
    metrics_details = generate_metrics_to_run()

    metrics_runners = list(map(lambda x: AccuracyMetricsRunner(x), metrics_details))

    basic_directory = "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/"
    dataset = "ml-100k"
    print("Datasets = {}".format(dataset))
    folds = (dataset, basic_directory + dataset + "/cross_validation/")

    LocalAccuracyMetricsRunner(metrics_runners).play(folds[1], folds[0])

    print("Finished")
