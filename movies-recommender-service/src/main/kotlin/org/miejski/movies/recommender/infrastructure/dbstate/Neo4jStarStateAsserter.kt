package org.miejski.movies.recommender.infrastructure.dbstate

import org.miejski.movies.recommender.domain.queries.QueriesLoader
import org.miejski.movies.recommender.infrastructure.dbstate.assertions.AssertionsContainer
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Neo4jStarStateAsserter @Autowired constructor(val cypherExecutor: CypherExecutor,
                                                    val assertions: AssertionsContainer) : InitializingBean {

    override fun afterPropertiesSet() {
        assertions.assertions().filter { !it.isOK() }
            .forEach {
                println("Executing query for assertion: ${it.name()}")
                val cypher = QueriesLoader().loadCypherQuery(it.queryToExecute())
                cypherExecutor.execute(cypher, it.queryToExecuteParams())
            }
    }

    fun run() {
        afterPropertiesSet()
    }
}