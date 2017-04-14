class Neo4jStateAssertions():

    def __init__(self, neo4j_query_executor, assertions, query_folder="/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api/movies-recommender-service/src/main/resources/cypher/start_state/"):
        self.query_folder = query_folder
        self.assertions = assertions
        self.neo4j_query_executor = neo4j_query_executor

    def run_assertions(self):
        for ass in self.assertions:
            if not ass.is_ok(self.neo4j_query_executor):
                cypher_file = "{}{}".format(self.query_folder, ass.query_to_execute())
                cypher = self.__read_cypher_from_file(cypher_file)
                self.neo4j_query_executor.invoke(cypher, ass.arguments())

    def __read_cypher_from_file(self, file):
        with open(file) as f:
            text = f.readlines()
            return ' '.join(text)
