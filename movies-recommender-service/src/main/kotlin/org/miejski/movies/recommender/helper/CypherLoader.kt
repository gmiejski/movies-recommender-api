package org.miejski.movies.recommender.helper

import org.springframework.stereotype.Component
import java.util.stream.Collectors.joining

@Component
class CypherLoader {

    companion object {
        val CYPHER_FILES_PATH: String = "cypher/"
    }

    fun loadCypherQuery(name: String): String {
        return this.javaClass.getClassLoader()
            .getResourceAsStream(toPath(name))
            .bufferedReader()
            .lines()
            .collect(joining(" "))
    }

    private fun toPath(cypherFileName: String): String {
        return CypherLoader.CYPHER_FILES_PATH + cypherFileName
    }
}