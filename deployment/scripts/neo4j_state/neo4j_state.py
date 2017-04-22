class Neo4jStateAssertions():

    def __init__(self, neo4j_query_executor, assertions, ):
        self.assertions = assertions
        self.neo4j_query_executor = neo4j_query_executor

    def run_assertions(self):
        for assertion in self.assertions:
            if not assertion.is_ok(self.neo4j_query_executor):
                print("Executing assertion: " + type(assertion).__name__)
                assertion.play(self.neo4j_query_executor)


