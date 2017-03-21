package org.miejski.movies.recommender.api

import org.miejski.movies.recommender.domain.recommendations.MovieRecommendation
import org.miejski.movies.recommender.domain.recommendations.RecommendationsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RecommendationsController @Autowired constructor(val recommendationsService: RecommendationsService) {

    @RequestMapping(value = "/recommendations/user/{userId}")
    fun getRecommendedMovies(@PathVariable("userId") userId: Long): List<MovieRecommendation> {
        return recommendationsService.findRecommendedMovies(userId)
    }
}