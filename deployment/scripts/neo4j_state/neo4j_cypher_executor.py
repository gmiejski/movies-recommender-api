from neo4j.v1 import GraphDatabase, basic_auth


class Neo4jCypherExecutor:

    def __init__(self):
        self.driver = GraphDatabase.driver("bolt://localhost:7699", auth=basic_auth("neo4j", "neo4j1234"))

    def invoke(self, cypher, args = {}):
        print( "Executing cypher : {}".format(cypher))
        session = self.driver.session()

        result = session.run(cypher, args)
        list_result = list(result)

        return list_result