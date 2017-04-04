package org.miejski.movies.recommender.infrastructure

import org.miejski.movies.recommender.api.user.dto.MovieRatingRequest
import org.miejski.movies.recommender.domain.movie.Movie
import org.miejski.movies.recommender.domain.user.Person
import org.miejski.movies.recommender.domain.user.UsersService
import org.miejski.movies.recommender.infrastructure.repositories.MovieRepository
import org.neo4j.ogm.session.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class DBSetup {

    @Autowired
    Session session

    @Autowired
    UsersService usersRepository

    @Autowired
    MovieRepository movieRepository

    @Autowired
    Neo4jOperations neo4jTemplate

    @Transactional
    def usersExists(long userId1, long userId2, double similarity) {
        if (usersRepository.findUserById(userId1) == null) {
            usersRepository.save(new Person(null, userId1, [], 1))
        }
        if (usersRepository.findUserById(userId2) == null) {
            usersRepository.save(new Person(null, userId2, [], 1))
        }

        neo4jTemplate.query("MERGE (b:Person {user_id: $userId1} ) " +
                "MERGE (c:Person {user_id: $userId2} )" +
                "MERGE (b)-[s:Similarity {similarity: $similarity}]-(c)".toString(), [:])
    }

    def averageRatings(@DelegatesTo(AverageRatingsObject) Closure closure) {
        def averageRatings = new AverageRatingsObject()
        def clone = closure.clone() as Closure
        clone.delegate = averageRatings
        clone.resolveStrategy = Closure.DELEGATE_ONLY
        clone()
    }

    @Transactional
    private def setAvgRating(long userId, double avgRating) {
        def user = usersRepository.findUserById(userId)
        if (user != null) {
            user.setAvg_rating(avgRating)
            usersRepository.save(user)
        }
    }

    def ratings(@DelegatesTo(MovieRatingsObject) Closure closure) {
        def moviesRatings = new MovieRatingsObject()
        def clone = closure.clone() as Closure
        clone.delegate = moviesRatings
        clone.resolveStrategy = Closure.DELEGATE_ONLY
        clone()
    }

    private class MovieRatingsObject {

        def rating(long userId, long movie, double rating) {
            if (!movieRepository.findAll().any { it.movie_id == movie }) {
                movieRepository.save(new Movie(null, movie))
            }
            usersRepository.rateMovie(userId, new MovieRatingRequest(movie, rating))
        }
    }

    private class AverageRatingsObject {
        def forUser(long userId, double avgRating) {
            setAvgRating(userId, avgRating)
        }
    }
}
