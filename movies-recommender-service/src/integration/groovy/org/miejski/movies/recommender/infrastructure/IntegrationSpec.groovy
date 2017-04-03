package org.miejski.movies.recommender.infrastructure

import com.jayway.restassured.RestAssured
import org.miejski.movies.recommender.Application
import org.miejski.movies.recommender.domain.user.UsersService
import org.miejski.movies.recommender.infrastructure.repositories.MovieRepository
import org.neo4j.ogm.session.Session
import org.neo4j.ogm.testutil.TestServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = [Application])
@IntegrationTest(["spring.profiles.active=integration", "server.port:0"])
@WebAppConfiguration
class IntegrationSpec extends Specification {

    private static TestServer testServer;

    @Value('${local.server.port}')
    protected int port

    @Autowired
    Environment environment

    @Autowired
    Session session

    @Autowired
    UsersService usersRepository

    @Autowired
    MovieRepository movieRepository

    @Autowired
    DBSetup dbSetup

    void setupSpec() {
        testServer = new TestServer.Builder()
                .enableAuthentication(false)
                .enableBolt(false)
                .transactionTimeoutSeconds(2)
                .port(7879)
                .build();

    }

    void setup() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        if (dbSetup != null) {
            dbSetup.graphDatabaseService = testServer.graphDatabaseService
        }
    }

    void cleanup() {
        session.clear()
        session.purgeDatabase()
    }

    void cleanupSpec() {
        if (testServer != null) {
            testServer.shutdown();
            testServer = null;
        }
    }
}
