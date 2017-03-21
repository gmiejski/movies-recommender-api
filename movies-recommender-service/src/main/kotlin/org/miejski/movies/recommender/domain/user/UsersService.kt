package org.miejski.movies.recommender.domain.user

import org.miejski.movies.recommender.api.user.dto.MovieRatingRequest
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

    fun getMeanRating(userId: Long): Double {
        return usersRepository.meanRating(userId)
    }

    fun findUserById(userId: Long): Person {
        return usersRepository.findOneByUserId(userId)
    }
}