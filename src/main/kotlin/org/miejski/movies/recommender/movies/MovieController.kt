package org.miejski.movies.recommender.movies

import org.miejski.movies.recommender.domain.Movie
import org.miejski.movies.recommender.domain.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MovieController @Autowired constructor(val movieRepository: MovieRepository) {

    @RequestMapping(value = "/movies/{movieId}")
    fun getUser(@PathVariable("movieId") userId: Long): Movie? {
        return movieRepository.findOne(userId)
    }

}