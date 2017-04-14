class Similarity1Assertion:

    def is_ok(self, Neo4jCypherExecutor):
        result = Neo4jCypherExecutor.invoke(self.__check_query())
        return len(result) > 0

    def query_to_execute(self):
        return "similarity_1.cypher"

    def arguments(self):
        return {}

    def __check_query(self):
        return "MATCH (p:Person)-[s:Similarity]->(p2:Person) return s limit 1"
