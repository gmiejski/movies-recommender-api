package org.miejski.movies.recommender.users

import org.miejski.movies.recommender.domain.Person
import org.miejski.movies.recommender.users.api.MovieRatingRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class UsersService @Autowired constructor(val usersRepository: UsersRepository) {
    fun findUser(userId: Long): Person {
        return usersRepository.findOne(userId)
    }

    fun findAll(): List<Person> = usersRepository.findAll().toList()

    fun findAllIds(): List<Int> = usersRepository.getAllIds()

    fun rateMovie(userId: Long, movieRating: MovieRatingRequest) {
        if (movieRating.movieId != null && movieRating.rating != null) {
            usersRepository.addMovieRating(userId, movieRating.rating, movieRating.movieId)
        }
    }
}