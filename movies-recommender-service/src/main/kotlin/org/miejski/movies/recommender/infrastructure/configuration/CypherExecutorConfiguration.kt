package org.miejski.movies.recommender.infrastructure.configuration

import org.miejski.movies.recommender.infrastructure.dbstate.CypherExecutor
import org.neo4j.ogm.session.Session
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CypherExecutorConfiguration {

    @Bean
    open fun cypherExecutor(session: Session): CypherExecutor {
        return object : CypherExecutor {
            override fun execute(cypher: String, queryToExecuteParams: Map<String, Object>) {
                session.query(cypher, queryToExecuteParams)
            }
        }
    }
}