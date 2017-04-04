package org.miejski.movies.recommender.infrastructure

import com.jayway.restassured.RestAssured
import org.miejski.movies.recommender.Application
import org.miejski.movies.recommender.domain.user.UsersService
import org.miejski.movies.recommender.infrastructure.repositories.MovieRepository
import org.neo4j.ogm.session.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.core.env.Environment
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@ActiveProfiles("integration")
@ContextConfiguration(loader = SpringApplicationContextLoader, classes = [Application])
@IntegrationTest(["spring.profiles.active=integration", "server.port:0"])
@WebAppConfiguration
class IntegrationSpec extends Specification {

    @LocalServerPort
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
    }

    void setup() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    void cleanup() {
        session.clear()
        session.purgeDatabase()
    }

    void cleanupSpec() {
    }
}
