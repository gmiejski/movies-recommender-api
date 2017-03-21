package org.miejski.movies.recommender.infrastructure.configuration

import org.neo4j.ogm.session.Session
import org.neo4j.ogm.session.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableNeo4jRepositories(basePackages = arrayOf("org.miejski.movies.recommender"))
@EnableTransactionManagement
open class Neo4jConfig {

    lateinit @Autowired var neo4JConfigProperties: Neo4jConfigProperties

    @Bean
    open fun sessionFactory(): SessionFactory {
        return SessionFactory(getConfiguration(), "org.miejski.movies.recommender.domain")
    }

    @Bean
    open fun transactionManager(): Neo4jTransactionManager {
        return Neo4jTransactionManager(sessionFactory())
    }

    @Bean
    open fun session(): Session {
        return sessionFactory().openSession()
    }

//    @Bean
//    open fun transactionManager(): Neo4jTransactionManager {
//        return Neo4jTransactionManager(sessionFactory())
//    }

    @Bean
    open fun getConfiguration(): org.neo4j.ogm.config.Configuration {
        val config: org.neo4j.ogm.config.Configuration = org.neo4j.ogm.config.Configuration();
        val buildUri = getBuildUri()
        config
            .driverConfiguration()
            .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
            .setURI(buildUri)
        return config;
    }

    private fun getBuildUri(): String {
        if (neo4JConfigProperties.user.isNullOrBlank() && neo4JConfigProperties.password.isNullOrBlank()) {
            return "http://${neo4JConfigProperties.host}:${neo4JConfigProperties.port}"
        }
        return "http://${neo4JConfigProperties.user}:${neo4JConfigProperties.password}@${neo4JConfigProperties.host}:${neo4JConfigProperties.port}"
    }
}