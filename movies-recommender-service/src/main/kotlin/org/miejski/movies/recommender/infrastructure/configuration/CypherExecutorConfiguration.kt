package org.miejski.movies.recommender.infrastructure.configuration

import org.miejski.movies.recommender.infrastructure.dbstate.CypherExecutor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.template.Neo4jOperations

@Configuration
open class CypherExecutorConfiguration {

    @Bean
    open fun cypherExecutor(neo4jOperations: Neo4jOperations): CypherExecutor {
        return object : CypherExecutor {
            override fun execute(cypher: String, queryToExecuteParams: Map<String, Object>) {
                neo4jOperations.query(cypher, queryToExecuteParams)
            }
        }
    }
}