package org.miejski.movies.recommender

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration
import org.springframework.context.annotation.Configuration

@SpringBootApplication(exclude = arrayOf(Neo4jDataAutoConfiguration::class))
@Configuration
open class Application {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}