package org.miejski.movies.recommender.infrastructure.dbstate.assertions

import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStateAssertion

interface AssertionsContainer {
    fun assertions(): List<Neo4jStateAssertion>
}