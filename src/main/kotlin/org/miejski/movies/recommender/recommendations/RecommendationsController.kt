package org.miejski.movies.recommender.recommendations

import org.miejski.movies.recommender.domain.Movie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class RecommendationsController @Autowired constructor(val recommendationsService: RecommendationsService) {
    fun getRecommendedMovies(): Movie? {
        return recommendationsService.findRecommendedMovies()
    }
}