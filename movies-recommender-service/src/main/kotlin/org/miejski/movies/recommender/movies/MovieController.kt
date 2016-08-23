package org.miejski.movies.recommender.movies

import org.miejski.movies.recommender.domain.Movie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController

@RestController
open class MovieController @Autowired constructor(val movieRepository: MovieRepository) {

    @RequestMapping(value = "/movies/{movieId}")
    fun getMovie(@PathVariable("movieId") movieId: Long): Movie? {
        return movieRepository.findOne(movieId)
    }

    @RequestMapping(value = "/movies/{movieId}",
        method = arrayOf(POST),
        consumes = arrayOf(APPLICATION_JSON_VALUE))
    fun createMovie(movie: Movie) {
        movieRepository.save(movie)
    }

    @RequestMapping(value = "/movies/ids")
    fun ids(): List<Int> {
        return movieRepository.getAllIds().map { it.toInt() }
    }
}