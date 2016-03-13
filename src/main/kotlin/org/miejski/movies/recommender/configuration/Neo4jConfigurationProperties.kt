package org.miejski.movies.recommender.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "neo4j")
class Neo4jConfigurationProperties {
    lateinit var host: String
    lateinit var port: String
    lateinit var user: String
    lateinit var password: String
}
