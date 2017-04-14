class PersonIndexAssertion:
    def is_ok(self, Neo4jCypherExecutor):
        return False

    def query_to_execute(self):
        return "person_index.cypher"

    def arguments(self):
        return {}
