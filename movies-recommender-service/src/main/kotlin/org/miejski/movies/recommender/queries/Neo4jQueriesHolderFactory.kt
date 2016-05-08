package org.miejski.movies.recommender.queries

import org.springframework.beans.factory.config.AbstractFactoryBean
import org.springframework.stereotype.Component
import java.util.stream.Collectors.joining

@Component
class Neo4jQueriesHolderFactory : AbstractFactoryBean<Neo4jQueriesHolder>() {

    companion object {
        val CYPHER_FILES_PATH: String = "cypher/"
    }

    @Override
    override fun getObjectType(): Class<*>? {
        return Neo4jQueriesHolder::class.java
    }

    @Override
    override fun createInstance(): Neo4jQueriesHolder? {
        val recommendationQuery = loadCypherQuery("approximated_neighbours_recommendation.cypher")

        return Neo4jQueriesHolder(mapOf(Pair("recommendationCypher", recommendationQuery)))
    }

    fun loadCypherQuery(name: String): String {
        val toPath = toPath(name)
        return this.javaClass.classLoader.getResourceAsStream(toPath)
            .bufferedReader()
            .lines()
            .collect(joining(" "))
    }

    private fun toPath(cypherFileName: String): String {
        return CYPHER_FILES_PATH + cypherFileName
    }
}