package org.miejski.movies.recommender.infrastructure.dbstate


interface Neo4jStateAssertion {

    fun name(): String

    fun queryToExecute(): String

    fun queryToExecuteParams(): Map<String, Object> = emptyMap()

    fun isOK(): Boolean {
        println("Neo4jStateAssertion: ${name()}")

        return false
    }
}