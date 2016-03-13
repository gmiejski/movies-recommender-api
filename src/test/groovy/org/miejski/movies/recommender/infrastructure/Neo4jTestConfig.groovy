package org.miejski.movies.recommender.infrastructure

import org.miejski.movies.recommender.InProcessServer
import org.neo4j.ogm.session.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.server.Neo4jServer
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class Neo4jTestConfig extends Neo4jConfiguration {

    @Autowired
    Environment environment

    @Bean
    @Override
    Neo4jServer neo4jServer() {
        return new InProcessServer()
    }

    @Bean
    @Override
    SessionFactory getSessionFactory() {
        return new SessionFactory("org.miejski.movies.recommender.domain")
    }
}
