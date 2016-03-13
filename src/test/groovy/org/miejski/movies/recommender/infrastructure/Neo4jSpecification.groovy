package org.miejski.movies.recommender.infrastructure

import org.miejski.movies.recommender.Application
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.EnableTransactionManagement
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = [Application, Neo4jTestConfig])
@IntegrationTest(["application.environment=integration", "server.port:0"])
@WebAppConfiguration
@Configuration
@EnableTransactionManagement
class Neo4jSpecification extends Specification {

    void setup() {
    }

    void cleanup() {


    }
}
