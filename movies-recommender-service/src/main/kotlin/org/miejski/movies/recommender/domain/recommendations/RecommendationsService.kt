package org.miejski.movies.recommender.domain.recommendations

import org.miejski.movies.recommender.helper.castTo
import org.neo4j.ogm.session.Session
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class RecommendationsService @Autowired constructor(
    val session: Session,
    val recommendationsQuery: RecommendationsQuery)
    : RecommendationsServiceI {

    private val logger = LoggerFactory.getLogger(RecommendationsService::class.java)

    override fun findRecommendedMovies(userId: Long, minSimilarity: Double, similarityMethod: String, neighboursCount: Int?): List<MovieRecommendation> {
        val queryTemplate = if (neighboursCount != null) {
            recommendationsQuery.getRecommendationWIthNBestNeighboursQuery()
        } else recommendationsQuery.getRecommendationQuery()

        val cypherQuery = queryTemplate.replace("{similarity_method}", similarityMethod)
            .replace("{n_best_neighbours}", neighboursCount.toString())


        val result = session.query(cypherQuery, mapOf(
            Pair("userId", userId),
            Pair("min_similarity", minSimilarity)))
            .castTo(MoviesPredictionScore::class.java)

        return findBestRecommendations(result).take(100)
    }

    private fun findBestRecommendations(neighboursPredictionScores: List<MoviesPredictionScore>): List<MovieRecommendation> {
        return neighboursPredictionScores.map {
            MovieRecommendation(it.movieId, it.prediction, it.movieNeighboursRatings.toDouble())
        }
    }

    override fun predictedRating(userId: Long, movieId: Long): Double {

        val cypherQuery = recommendationsQuery.getPredictionQuery()

        val query = session.query(cypherQuery, mapOf(
            Pair("userId", userId),
            Pair("movieId", movieId)))

        val get = query.firstOrNull()?.get("prediction")?.toString()?.toDouble()
        if (get != null) {
            return get.toString().toDouble()
        }
        logger.info("Predicting rating - returning default value for user {} and movie {}", userId, movieId)
        return -1.0
    }
}

data class MoviesPredictionScore(val movieId: Long, val prediction: Double, val movieNeighboursRatings: Long)

data class MovieRecommendation(val movieId: Long, val prediction: Double, val score: Double)

interface RecommendationsServiceI {
    fun findRecommendedMovies(userId: Long, minSimilarity: Double = 0.6, similarityMethod: String = "similarity", neighboursCount: Int? = null): List<MovieRecommendation>
    fun predictedRating(userId: Long, movieId: Long): Double
}