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

    override fun findRecommendedMovies(userId: Long, minSimilarity: Double): List<MovieRecommendation> {
        val cypherQuery = recommendationsQuery.getRecommendationQuery()

        val result = session.query(cypherQuery, mapOf(Pair("userId", userId), Pair("min_similarity", minSimilarity)))
            .castTo(MoviesPredictionScore::class.java)

        return findBestRecommendations(result).sortedByDescending { it.score }.take(100)
    }

    private fun findBestRecommendations(neighboursPredictionScores: List<MoviesPredictionScore>): List<MovieRecommendation> {
        return neighboursPredictionScores.map {
            MovieRecommendation(it.movieId, it.prediction, it.movieNeighboursRatings.toDouble())
        }
    }

    private fun calculateScore(prediction: Double, sharedRatings: Long, maxSharedRatings: Long?): Double {
        if (maxSharedRatings != null) {
            return prediction * Math.cos((maxSharedRatings.toDouble() - sharedRatings.toDouble()) / maxSharedRatings.toDouble())
        } else {
            return 0.0
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
    fun findRecommendedMovies(userId: Long, minSimilarity: Double = 0.6): List<MovieRecommendation>
    fun predictedRating(userId: Long, movieId: Long): Double
}