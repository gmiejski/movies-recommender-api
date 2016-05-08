package org.miejski.movies.recommender.infrastructure

import com.jayway.restassured.RestAssured
import org.junit.Rule
import org.miejski.movies.recommender.Application
import org.neo4j.ogm.testutil.Neo4jIntegrationTestRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Shared
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = [Application])
@IntegrationTest(["spring.profiles.active=integration", "server.port:0"])
@WebAppConfiguration
class IntegrationSpec extends Specification {

    @Rule
    @Shared
    final static Neo4jIntegrationTestRule neo4jRule = new Neo4jIntegrationTestRule(7879);

    @Value('${local.server.port}')
    protected int port

    @Autowired
    Environment environment

    void setup() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    void cleanup() {
        neo4jRule.clearDatabase()
    }
}
