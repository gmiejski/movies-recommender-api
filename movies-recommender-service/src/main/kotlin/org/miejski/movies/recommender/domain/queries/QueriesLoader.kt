package org.miejski.movies.recommender.domain.queries

import java.util.stream.Collectors

open class QueriesLoader {

    companion object {
        val CYPHER_FILES_PATH: String = "cypher/"
    }

    open fun loadCypherQuery(name: String): String {
        val toPath = toPath(name)
        return this.javaClass.classLoader.getResourceAsStream(toPath)
            .bufferedReader()
            .lines()
            .collect(Collectors.joining(" "))
    }

    open fun toPath(cypherFileName: String): String {
        return CYPHER_FILES_PATH + cypherFileName
    }

}