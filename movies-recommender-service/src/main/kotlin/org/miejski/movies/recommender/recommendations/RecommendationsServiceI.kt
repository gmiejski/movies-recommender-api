package org.miejski.movies.recommender.recommendations

interface RecommendationsServiceI {
    fun findRecommendedMovies(userId: Long): List<MovieRecommendation>
    fun predictedRating(userId: Long, movieId: Long): Double
}