import os

from neo4j_state.assertions.simple_cypher_state_assertion import SimpleCypherStateAssertion
from neo4j_state.neo4j_cypher_executor import Neo4jCypherExecutor
from ratings_in_common.ratings_in_common import RatingsInCommon


class PearsonWithSWAssertion(SimpleCypherStateAssertion):
    def __init__(self, train_file, fold, rerun=True):
        self.rerun = rerun
        self.train_file = train_file
        self.fold = fold

    def play(self, Neo4jCypherExecutor):
        data = PearsonWithSWAssertion.loadRatingsData(self.train_file)
        RatingsInCommon().results(data, self.__movies_in_common_file_path())

        self.execute_query(Neo4jCypherExecutor, self.query_to_execute(), self.arguments())

        self.__remove_common_movies_file()

    def is_ok(self, Neo4jCypherExecutor):
        if self.rerun:
            return False
        result = Neo4jCypherExecutor.invoke(self.__check_query())
        return len(result) > 0

    def query_to_execute(self):
        return "similarity_pearson_with_sw.cypher"

    def arguments(self):
        return {"moviesInCommonFile": "file://{}".format(self.__movies_in_common_file_path())}

    def __check_query(self):
        return "MATCH (p:Person)-[s:Similarity]->(p2:Person) where exists(s.pearson_with_sw) return s limit 1"

    def __movies_in_common_file_path(self):
        return "{}-movies_in_common".format(self.train_file)

    @staticmethod
    def loadRatingsData(path):
        with open(path) as f:
            lines = f.readlines()
            return list(
                map(lambda line_splited: (line_splited[0], line_splited[1]),
                    map(lambda x: x.split("\t"),
                        lines[1:])))

    def __remove_common_movies_file(self):
        os.remove(self.__movies_in_common_file_path())


if __name__ == "__main__":
    a = PearsonWithSWAssertion(
        "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_train_4",
        "ml-100k_train_4")

    executor = Neo4jCypherExecutor()
    if not a.is_ok(executor):
        a.play(executor)
