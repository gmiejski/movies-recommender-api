from neo4j_state.assertions.simple_cypher_state_assertion import SimpleCypherStateAssertion


class AverageRatingAssertion(SimpleCypherStateAssertion):

    def play(self, Neo4jCypherExecutor):
        self.execute_query(Neo4jCypherExecutor, self.query_to_execute(), self.arguments())

    def is_ok(self, Neo4jCypherExecutor):
        result = Neo4jCypherExecutor.invoke(self.__check_query())
        return len(result) > 0

    def query_to_execute(self):
        return "average_rating.cypher"

    def arguments(self):
        return {}

    def __check_query(self):
        return "Match (p:Person) where exists(p.avg_rating) return p limit 1"
