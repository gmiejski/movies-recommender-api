

class SimpleCypherStateAssertion:
    query_folder = "/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api/movies-recommender-service/src/main/resources/cypher/start_state/"

    def execute_query(self, neo4j_query_executor, query_to_execute, arguments = {}):
        cypher_file = "{}{}".format(SimpleCypherStateAssertion.query_folder, query_to_execute)
        cypher = self.__read_cypher_from_file(cypher_file)
        neo4j_query_executor.invoke(cypher, arguments)

    def __read_cypher_from_file(self, file):
        with open(file) as f:
            text = f.readlines()
            return ' '.join(text)
