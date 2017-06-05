package org.miejski.movies.recommender.infrastructure

import com.jayway.restassured.RestAssured
import org.miejski.movies.recommender.Application
import org.miejski.movies.recommender.domain.user.UsersService
import org.miejski.movies.recommender.infrastructure.repositories.MovieRepository
import org.neo4j.ogm.session.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("integration")
@SpringBootTest(classes = [Application], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
