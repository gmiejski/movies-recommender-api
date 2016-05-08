package org.miejski.movies.recommender.queries

class Neo4jQueriesHolder(private val queriesByName: Map<String, String>) {

    fun findQuery(name: String): String {
        return queriesByName.get(name).orEmpty() // TODO
    }

    fun recommendationCypher(): String {
        return queriesByName.get("recommendationCypher").orEmpty()
    }
}