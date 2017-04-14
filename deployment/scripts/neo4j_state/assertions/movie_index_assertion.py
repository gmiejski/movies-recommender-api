class Neo4jMovieIndexAssertion:
    def is_ok(self, Neo4jCypherExecutor):
        return False

    def query_to_execute(self):
        return "movie_index.cypher"

    def __check_query(self):
        return "MATCH (a:Person) limit 1 return a"

    def arguments(self):
        return {}