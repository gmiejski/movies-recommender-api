package org.miejski.movies.recommender.infrastructure.configuration

import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.EnableTransactionManagement


@EnableNeo4jRepositories(basePackages = arrayOf("org.miejski.movies.recommender.infrastructure.repositories"))
@EnableTransactionManagement
@org.springframework.context.annotation.Configuration
@Component
open class Neo4jConfig {

    lateinit @Autowired var neo4JConfigProperties: Neo4jConfigProperties

    @Bean
    open fun getConfiguration(): Configuration {
        val config: Configuration = Configuration()
        val buildUri = getBuildUri()
        config
            .driverConfiguration()
            .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
            .setURI(buildUri)
        return config
    }

    private fun getBuildUri(): String {
        if (neo4JConfigProperties.user.isNullOrBlank() && neo4JConfigProperties.password.isNullOrBlank()) {
            return "http://${neo4JConfigProperties.host}:${neo4JConfigProperties.port}"
        }
        return "http://${neo4JConfigProperties.user}:${neo4JConfigProperties.password}@${neo4JConfigProperties.host}:${neo4JConfigProperties.port}"
    }

    @Bean
    open fun getSessionFactory(): SessionFactory {
        return SessionFactory(getConfiguration(), "org.miejski.movies.recommender.domain")
    }

    @Bean
    open fun neo4jTransactionManager(): Neo4jTransactionManager {
        return Neo4jTransactionManager(getSessionFactory())
    }

}