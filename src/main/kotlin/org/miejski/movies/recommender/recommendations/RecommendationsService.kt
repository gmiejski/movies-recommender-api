package org.miejski.movies.recommender.recommendations

import org.miejski.movies.recommender.helper.CypherLoader
import org.miejski.movies.recommender.helper.castTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import org.springframework.stereotype.Service

@Service
class RecommendationsService @Autowired constructor(val neo4jOperations: Neo4jOperations,
                                                    val cypherLoader: CypherLoader) {
    fun findRecommendedMovies(userId: Long): List<MovieRecommendation> {

        val cypherQuery = cypherLoader.loadCypherQuery("approximated_neighbours_recommendation.cypher")
        val result = neo4jOperations.query(cypherQuery, mapOf(Pair("userId", userId)))
            .castTo(MoviesPredictionScore::class.java)

        return findBestRecommendations(result).sortedByDescending { it.score }
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
        if ( maxSharedRatings != null) {
            return prediction * Math.cos((maxSharedRatings.toDouble() - sharedRatings.toDouble()) / maxSharedRatings.toDouble())
        } else {
            return 0.0
        }

    }

}

data class CypherResult(val neighbour: Int, val sharedRatings: Int, val a: Int)

data class MoviesPredictionScore(val prediction: Double, val movie_id: Long, val ratings_count: Long)

data class MovieRecommendation(val movieId: Long, val prediction: Double, val score: Double)