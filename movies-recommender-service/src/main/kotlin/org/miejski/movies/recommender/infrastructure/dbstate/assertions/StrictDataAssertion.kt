package org.miejski.movies.recommender.infrastructure.dbstate.assertions

import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStateAssertion
import org.springframework.data.neo4j.template.Neo4jOperations
import java.util.*

class StrictDataAssertion(val neo4jOperations: Neo4jOperations) : Neo4jStateAssertion {

    override fun isOK(): Boolean {
        if (neo4jOperations.query("Match (p:Person) return p limit 1", HashMap<String, Object>()).count() == 0) {
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