package org.miejski.movies.recommender.infrastructure.dbstate.assertions

import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStateAssertion
import org.springframework.data.neo4j.template.Neo4jOperations
import java.util.*

class Similarity_1_Assertion(val neo4jOperations: Neo4jOperations) : Neo4jStateAssertion {
    override fun name(): String {
        return "Similarity_1_Assertion"
    }

    override fun queryToExecute(): String {
        return "start_state/similarity_1.cypher"
    }

    override fun isOK(): Boolean {
        return neo4jOperations.query("Match (p:Person)-[s:Similarity]-(p2:Person) where exists(s.similarity) return s limit 1", HashMap<String, Object>()).count() > 0
    }
}