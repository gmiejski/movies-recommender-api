package org.miejski.movies.recommender.api

import org.miejski.movies.recommender.domain.recommendations.MovieRecommendation
import org.miejski.movies.recommender.domain.recommendations.RecommendationsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RecommendationsController @Autowired constructor(val recommendationsService: RecommendationsService) {

    private val logger = LoggerFactory.getLogger(RecommendationsController::class.java)

    @RequestMapping(value = "/recommendations/user/{userId}")
    fun getRecommendedMovies(@PathVariable("userId") userId: Long,
                             @RequestParam(name = "minSimilarity", required = false) minSimilarity: Double?,
                             @RequestParam(name = "similarityMethod", required = false) similarityMethod: String?,
                             @RequestParam(name = "neighboursCount", required = false) neighboursCount: Int?): List<MovieRecommendation> {
        val minSim = minSimilarity ?: 0.0
        val simMethod = similarityMethod ?: "similarity"
        val recommendations = recommendationsService.findRecommendedMovies(userId, minSim, simMethod, neighboursCount)
        logger.info("Movies: ${recommendations.size}, minSimilarity: $minSim, similarityMethod: $simMethod, neighboursCount: $neighboursCount, userId: $userId")
        if ( recommendations.isEmpty()) {
            throw RuntimeException("Error - recommendations are empty for user $userId!")
        }
        return recommendations
    }
}