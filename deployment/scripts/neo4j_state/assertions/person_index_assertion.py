from neo4j_state.assertions.simple_cypher_state_assertion import SimpleCypherStateAssertion


class PersonIndexAssertion(SimpleCypherStateAssertion):

    def play(self, Neo4jCypherExecutor):
        self.execute_query(Neo4jCypherExecutor, self.query_to_execute(), self.arguments())

    def is_ok(self, Neo4jCypherExecutor):
        return False

    def query_to_execute(self):
        return "person_index.cypher"

    def arguments(self):
        return {}
