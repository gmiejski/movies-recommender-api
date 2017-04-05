package org.miejski.movies.recommender.infrastructure.dbstate

interface CypherExecutor {

    fun execute(cypher: String, queryToExecuteParams: Map<String, Object>)

}