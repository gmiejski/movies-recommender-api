class DataLoadedAssertion:

    def __init__(self, train_file):
        self.train_file = train_file

    def is_ok(self, Neo4jCypherExecutor):
        result = Neo4jCypherExecutor.invoke(self.__check_query())
        return len(result) > 0

    def query_to_execute(self):
        return "import_data.cypher"

    def arguments(self):
        return {"trainingDataFile": "file://{}".format(self.train_file)}

    def __check_query(self):
        return "MATCH (a:Person) return a limit 1"
