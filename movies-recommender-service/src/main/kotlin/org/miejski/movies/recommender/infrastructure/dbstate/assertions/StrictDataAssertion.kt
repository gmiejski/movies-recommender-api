package org.miejski.movies.recommender.infrastructure.dbstate.assertions

import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStateAssertion
import org.neo4j.ogm.session.Session
import java.util.*

class StrictDataAssertion(val session: Session) : Neo4jStateAssertion {

    override fun isOK(): Boolean {
        if (session.query("Match (p:Person) return p limit 1", HashMap<String, Object>()).count() == 0) {
            throw RuntimeException("StrictDataAssertion fail - no Persons in database!")
        }

        return true
    }

    override fun queryToExecute(): String {
        TODO("Shouldn't be called at all")
    }

    override fun name(): String {
        return "StrictDataAssertion"
    }
}