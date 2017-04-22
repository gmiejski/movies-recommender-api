from neo4j.v1 import GraphDatabase, basic_auth


class Neo4jCypherExecutor:

    def __init__(self, localhost="bolt://localhost:7699"):
        self.driver = GraphDatabase.driver(localhost, auth=basic_auth("neo4j", "neo4j1234"))

    def invoke(self, cypher, args = {}):
        print( "Executing cypher : {}".format(cypher))
        session = self.driver.session()

        result = session.run(cypher, args)
        list_result = list(result)

        return list_result