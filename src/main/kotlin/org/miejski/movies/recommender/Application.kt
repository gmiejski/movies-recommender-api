package org.miejski.movies.recommender

import org.neo4j.ogm.session.Neo4jSession
import org.neo4j.ogm.session.Session
import org.neo4j.ogm.session.SessionFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.core.env.Environment
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.data.neo4j.server.Neo4jServer
import org.springframework.data.neo4j.server.RemoteServer
import org.springframework.data.neo4j.template.Neo4jOperations
import org.springframework.data.neo4j.template.Neo4jTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableNeo4jRepositories(basePackages = arrayOf(
    "org.miejski.movies.recommender.users",
    "org.miejski.movies.recommender.movies"))
@EnableTransactionManagement
@Configuration
open class Application() {
    companion object {
        @JvmStatic public fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}