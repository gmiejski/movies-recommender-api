package org.miejski.movies.recommender.infrastructure.configuration

import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement


@EnableNeo4jRepositories(basePackages = arrayOf("org.miejski.movies.recommender.infrastructure.repositories"))
@EnableTransactionManagement
@Profile("integration")
@org.springframework.context.annotation.Configuration
open class Neo4jConfigIntegration : Neo4jConfiguration() {

    lateinit @Autowired var neo4JConfigProperties: Neo4jConfigProperties

    @Bean
    open fun getConfiguration(): Configuration {
        val config: Configuration = Configuration()
        config
            .driverConfiguration()
            .setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver")
        return config
    }

    @Bean
    override fun getSessionFactory(): SessionFactory {
        return SessionFactory(getConfiguration(), "org.miejski.movies.recommender.domain")
    }
}