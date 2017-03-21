package org.miejski.movies.recommender.domain.recommendations

import org.miejski.movies.recommender.helper.castTo
import org.miejski.movies.recommender.queries.Neo4jQueriesHolder
import org.neo4j.ogm.session.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class RecommendationsService @Autowired constructor(val session: Session,
                                                         val neo4jQueriesHolder: Neo4jQueriesHolder) : RecommendationsServiceI {
    override fun findRecommendedMovies(userId: Long): List<MovieRecommendation> {

        val cypherQuery = neo4jQueriesHolder.recommendationCypher()

        val result = session.query(cypherQuery, mapOf(Pair("userId", userId)))
            .castTo(MoviesPredictionScore::class.java)

        return findBestRecommendations(result).sortedByDescending { it.score }.take(100)
    }

    private fun findBestRecommendations(neighboursPredictionScores: List<MoviesPredictionScore>): List<MovieRecommendation> {
        val maxSharedRatings = neighboursPredictionScores.maxBy { it.ratings_count }?.ratings_count
        return neighboursPredictionScores.map {
            MovieRecommendation(
                it.movie_id,
                it.prediction,
                calculateScore(it.prediction, it.ratings_count, maxSharedRatings))
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
        val cypherQuery = neo4jQueriesHolder.findQuery("predictedRating")

        val query = session.query(cypherQuery, mapOf(
            Pair("userId", userId),
            Pair("movieId", movieId)))

        val get = query.first().get("predictedRating")
        if (get != null) {
            return get.toString().toDouble()
        }
        return -1.0
    }
}

data class MoviesPredictionScore(val prediction: Double, val movie_id: Long, val ratings_count: Long)

data class MovieRecommendation(val movieId: Long, val prediction: Double, val score: Double)

interface RecommendationsServiceI {
    fun findRecommendedMovies(userId: Long): List<MovieRecommendation>
    fun predictedRating(userId: Long, movieId: Long): Double
}