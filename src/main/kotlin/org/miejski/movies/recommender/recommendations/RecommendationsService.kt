package org.miejski.movies.recommender.recommendations

import org.miejski.movies.recommender.domain.Movie
import org.miejski.movies.recommender.movies.MovieRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RecommendationsService @Autowired constructor(val movieRepository: MovieRepository) {
    fun findRecommendedMovies(): Movie? {
        val findOne = movieRepository.findOne(4784)
        return findOne
    }
}