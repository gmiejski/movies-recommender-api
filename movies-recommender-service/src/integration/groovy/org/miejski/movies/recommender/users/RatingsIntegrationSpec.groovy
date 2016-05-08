package org.miejski.movies.recommender.users

import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import org.miejski.movies.recommender.infrastructure.IntegrationSpec
import org.miejski.movies.recommender.users.api.MovieRatingRequest

class RatingsIntegrationSpec extends IntegrationSpec {

    def "should successfully rate a movie"() {
        given:
        def userId = 123
        def request = new MovieRatingRequest("123", 3.5)

        when:
        def response = RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post("/users/${userId}/ratings".toString())
                .then()

        then:
        response.statusCode(200)
    }
}
