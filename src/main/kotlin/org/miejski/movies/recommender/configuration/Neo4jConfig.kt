package org.miejski.movies.recommender.configuration

import org.neo4j.ogm.session.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.server.Neo4jServer
import org.springframework.data.neo4j.server.RemoteServer

@Configuration
open class Neo4jConfig : Neo4jConfiguration() {

    lateinit @Autowired var neo4JConfigProperties: Neo4jConfigProperties

    @Bean
    override fun getSessionFactory(): SessionFactory {
        return SessionFactory("org.miejski.movies.recommender.domain")
    }

    @Bean
    override fun neo4jServer(): Neo4jServer {
        return RemoteServer(neo4JConfigProperties.host + ":" + neo4JConfigProperties.port,
            neo4JConfigProperties.user,
            neo4JConfigProperties.password)
    }

}