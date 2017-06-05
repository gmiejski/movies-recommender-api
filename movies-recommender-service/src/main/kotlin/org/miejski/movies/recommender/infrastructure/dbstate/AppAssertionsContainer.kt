package org.miejski.movies.recommender.infrastructure.dbstate

import org.miejski.movies.recommender.infrastructure.dbstate.assertions.AssertionsContainer
import org.neo4j.ogm.session.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class AppAssertionsContainer @Autowired constructor(val session: Session) : AssertionsContainer {

    override fun assertions(): List<Neo4jStateAssertion> {
        return emptyList()
    }
}