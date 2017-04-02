package org.miejski.movies.recommender.users

import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import org.miejski.movies.recommender.api.user.dto.MovieRatingRequest
import org.miejski.movies.recommender.domain.movie.Movie
import org.miejski.movies.recommender.domain.user.Person
import org.miejski.movies.recommender.infrastructure.IntegrationSpec
import org.miejski.movies.recommender.infrastructure.repositories.MovieRepository
import org.miejski.movies.recommender.infrastructure.repositories.UsersRepository
import org.springframework.beans.factory.annotation.Autowired

class RatingsIntegrationIT extends IntegrationSpec {

    @Autowired
    UsersRepository usersRepository

    @Autowired
    MovieRepository movieRepository

    def "should successfully rate a movie"() {
        given:
        def userId = 123
        def movieId = 12
        usersRepository.save(new Person(null, userId, []))
        movieRepository.save(new Movie(null, movieId))
        def request = new MovieRatingRequest(movieId, 3.5)

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
