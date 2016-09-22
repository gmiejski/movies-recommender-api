package org.miejski.movies.recommender.recommendations

import org.miejski.movies.recommender.helper.castTo
import org.miejski.movies.recommender.queries.Neo4jQueriesHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import org.springframework.stereotype.Service

@Service
class RecommendationsService @Autowired constructor(val neo4jOperations: Neo4jOperations,
                                                    val neo4jQueriesHolder: Neo4jQueriesHolder) {
    fun findRecommendedMovies(userId: Long): List<MovieRecommendation> {

        val cypherQuery = neo4jQueriesHolder.recommendationCypher()

        val result = neo4jOperations.query(cypherQuery, mapOf(Pair("userId", userId)))
            .castTo(MoviesPredictionScore::class.java)

        return findBestRecommendations(result).sortedByDescending { it.score }.take(10)
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

    fun predictedRating(userId: String, movieId: String): Double {
        val cypherQuery = neo4jQueriesHolder.findQuery("predictedRating")

        val query = neo4jOperations.query(cypherQuery, mapOf(Pair("userId", userId.toLong()), Pair("movieId", movieId.toLong())))
        println()
        return query.first().get("predictedRating").toString().toDouble()
    }
}

data class MoviesPredictionScore(val prediction: Double, val movie_id: Long, val ratings_count: Long)

data class MovieRecommendation(val movieId: Long, val prediction: Double, val score: Double)