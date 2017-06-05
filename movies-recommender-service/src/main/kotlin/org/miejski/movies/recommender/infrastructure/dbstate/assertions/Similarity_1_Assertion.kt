package org.miejski.movies.recommender.infrastructure.dbstate.assertions

import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStateAssertion
import org.neo4j.ogm.session.Session
import java.util.*

class Similarity_1_Assertion(val session: Session) : Neo4jStateAssertion {
    override fun name(): String {
        return "Similarity_1_Assertion"
    }

    override fun queryToExecute(): String {
        return "start_state/similarity_pearson.cypher"
    }

    override fun isOK(): Boolean {
        return session.query("Match (p:Person)-[s:Similarity]-(p2:Person) where exists(s.similarity) return s limit 1", HashMap<String, Object>()).count() > 0
    }
}