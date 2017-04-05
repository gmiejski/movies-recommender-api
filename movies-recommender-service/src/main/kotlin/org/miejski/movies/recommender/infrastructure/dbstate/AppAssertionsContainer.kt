package org.miejski.movies.recommender.infrastructure.dbstate

import org.miejski.movies.recommender.infrastructure.dbstate.assertions.AssertionsContainer
import org.miejski.movies.recommender.infrastructure.dbstate.assertions.MovieIndexAssertion
import org.miejski.movies.recommender.infrastructure.dbstate.assertions.PersonIndexAssertion
import org.miejski.movies.recommender.infrastructure.dbstate.assertions.StrictDataAssertion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import org.springframework.stereotype.Component

@Component
class AppAssertionsContainer @Autowired constructor(val neo4jTemplate: Neo4jOperations) : AssertionsContainer {

    override fun assertions(): List<Neo4jStateAssertion> {
        return listOf(PersonIndexAssertion(),
            MovieIndexAssertion(),
            StrictDataAssertion(neo4jTemplate)
        )
    }
}