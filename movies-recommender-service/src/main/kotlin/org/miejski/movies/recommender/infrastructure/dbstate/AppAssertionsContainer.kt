package org.miejski.movies.recommender.infrastructure.dbstate

import org.miejski.movies.recommender.infrastructure.dbstate.assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import org.springframework.stereotype.Component

@Component
class AppAssertionsContainer @Autowired constructor(val neo4jTemplate: Neo4jOperations) : AssertionsContainer {

    override fun assertions(): List<Neo4jStateAssertion> {
        return listOf(PersonIndexAssertion(),
            MovieIndexAssertion(),
            StrictDataAssertion(neo4jTemplate),
            Similarity_1_Assertion(neo4jTemplate)
        )
    }
}