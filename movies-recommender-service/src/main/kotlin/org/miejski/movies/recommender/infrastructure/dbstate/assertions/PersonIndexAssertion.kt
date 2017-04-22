package org.miejski.movies.recommender.infrastructure.dbstate.assertions

import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStateAssertion

class PersonIndexAssertion : Neo4jStateAssertion {

    override fun isOK(): Boolean {
        return false
    }

    override fun queryToExecute(): String {
        return "start_state/person_index.cypher"
    }

    override fun name(): String {
        return "PersonIndexAssertion"
    }
}