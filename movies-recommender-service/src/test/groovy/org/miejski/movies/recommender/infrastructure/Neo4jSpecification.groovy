package org.miejski.movies.recommender.infrastructure

import org.junit.Rule
import org.miejski.movies.recommender.Application
import org.neo4j.ogm.testutil.Neo4jIntegrationTestRule
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Shared
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = [Application])
@IntegrationTest(["spring.profiles.active=integration", "server.port:0"])
@WebAppConfiguration
@Configuration
class Neo4jSpecification extends Specification {

    @Rule
    @Shared
    final static Neo4jIntegrationTestRule neo4jRule = new Neo4jIntegrationTestRule(7879);

}
