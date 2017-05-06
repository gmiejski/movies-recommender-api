package org.miejski.movies.recommender.infrastructure.dbstate

import org.miejski.movies.recommender.infrastructure.dbstate.assertions.AssertionsContainer
import org.miejski.movies.recommender.infrastructure.dbstate.assertions.MovieIndexAssertion
import org.miejski.movies.recommender.infrastructure.dbstate.assertions.PersonIndexAssertion
import org.miejski.movies.recommender.infrastructure.dbstate.assertions.Similarity_1_Assertion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import org.springframework.stereotype.Component

@Component
class AppAssertionsContainer @Autowired constructor(val neo4jTemplate: Neo4jOperations) : AssertionsContainer {

    override fun assertions(): List<Neo4jStateAssertion> {
        return emptyList()
        return listOf(PersonIndexAssertion(),
            MovieIndexAssertion(),
//            StrictDataAssertion(neo4jTemplate),
            Similarity_1_Assertion(neo4jTemplate)
        )
    }
}