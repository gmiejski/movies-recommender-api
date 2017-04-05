package org.miejski.movies.recommender.infrastructure.dbstate.assertions

import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStateAssertion

class MovieIndexAssertion : Neo4jStateAssertion {

    override fun isOK(): Boolean {
        super.isOK()
        return false
    }

    override fun queryToExecute(): String {
        return "start_state/movie_index.cypher"
    }

    override fun name(): String {
        return "MovieIndexAssertion"
    }
}