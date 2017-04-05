package org.miejski.movies.recommender.infrastructure.dbstate.assertions

import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStateAssertion

class Similarity_1_Assertion : Neo4jStateAssertion {
    override fun name(): String {
        return "Similarity_1_Assertion"
    }

    override fun queryToExecute(): String {
        return "start_state/similarity_1.cypher"
    }

    override fun isOK(): Boolean {
        return super.isOK()
    }
}