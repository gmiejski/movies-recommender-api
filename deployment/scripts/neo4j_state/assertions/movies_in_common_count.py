class Similarity1Assertion:
    def __init__(self, moviesInCommonFile):
        self.moviesInCommonFile = moviesInCommonFile

    def is_ok(self, Neo4jCypherExecutor):
        result = Neo4jCypherExecutor.invoke(self.__check_query())
        return len(result) > 0

    def query_to_execute(self):
        return "movies_in_common.cypher"

    def arguments(self):
        return {"moviesInCommonFile": "file://{}".format(self.moviesInCommonFile)}

    def __check_query(self):
        return "MATCH (p:Person)-[s:Similarity]->(p2:Person) where exists(s.movies_in_common) return s limit 1"
