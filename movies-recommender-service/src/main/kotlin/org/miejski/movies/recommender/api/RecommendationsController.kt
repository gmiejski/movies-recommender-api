package org.miejski.movies.recommender.api

import org.miejski.movies.recommender.domain.recommendations.MovieRecommendation
import org.miejski.movies.recommender.domain.recommendations.RecommendationsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RecommendationsController @Autowired constructor(val recommendationsService: RecommendationsService) {

    @RequestMapping(value = "/recommendations/user/{userId}")
    fun getRecommendedMovies(@PathVariable("userId") userId: Long,
                             @RequestParam(name = "minSimilarity", required = false) minSimilarity: Double?,
                             @RequestParam(name = "similarityMethod", required = false) similarityMethod: String?): List<MovieRecommendation> {
        val minSim = minSimilarity ?: 0.5
        val simMethod = similarityMethod ?: "similarity"
        val recommendations = recommendationsService.findRecommendedMovies(userId, minSim, simMethod)
        println("Movies: ${recommendations.size}, minSimilarity: $minSim, similarityMethod: ${simMethod}")
        return recommendations
    }
}