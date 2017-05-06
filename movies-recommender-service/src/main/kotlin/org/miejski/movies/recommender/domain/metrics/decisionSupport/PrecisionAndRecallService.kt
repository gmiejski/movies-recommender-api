package org.miejski.movies.recommender.domain.metrics.decisionSupport

import org.miejski.movies.recommender.api.metrics.MetricsResult
import org.miejski.movies.recommender.domain.metrics.MetricsService
import org.miejski.movies.recommender.domain.metrics.accuracy.RealRating
import org.miejski.movies.recommender.domain.recommendations.MovieRecommendation
import org.miejski.movies.recommender.domain.recommendations.RecommendationsServiceI
import org.miejski.movies.recommender.domain.user.UsersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class PrecisionAndRecallService @Autowired constructor(
    val recommendationsService: RecommendationsServiceI,
    val userService: UsersService)
: MetricsService<Pair<Double, Double>>() {
    private var precisionSupportAccumulator: DecisionSupportAccumulator = DecisionSupportAccumulator()

    override fun run(realRatings: List<RealRating>): Double {
        start()
        val userRatings = realRatings.groupBy { it.person }

        val usersWithRatingsAndReco: List<Triple<Long, List<RealRating>, List<MovieRecommendation>>> = runAsyncAndGather(
            userRatings.toList(), { Triple(it.first, it.second, recommendationsService.findRecommendedMovies(it.first, neighboursCount = null)) })

        val recoMapByUser = usersWithRatingsAndReco.map { Pair(it.first, it.third) }.toMap()

        val usersMeanRatings: Map<Long, Double> = runAsyncAndGather(
            userRatings.keys.toList(),
            { Pair(it, userService.getMeanRating(it)) })
            .toMap()

        val moviesLikedByUsers = getMoviesLikedByUsers(userRatings, usersMeanRatings)

        val goodRecommendationsCountPerUser: Map<Long, Int> = calculateGoodRecommendationsCount(
            moviesLikedByUsers,
            usersWithRatingsAndReco)

        val precisionPerUser = recoMapByUser.map { goodRecommendationsCountPerUser.get(it.key)!!.toDouble() / recoMapByUser.get(it.key)!!.count() }

        val recallPerUser = recoMapByUser
            .map { Pair(it.key, goodRecommendationsCountPerUser.get(it.key)!!.toDouble() / moviesLikedByUsers.get(it.key)!!.count()) }
            .map { Pair(it.first, if (it.second.equals(Double.NaN)) 0.0 else it.second) }

        precisionSupportAccumulator.saveResult(precisionPerUser.average(), recallPerUser.map { it.second }.average(), timeInSeconds())

        return precisionPerUser.average()
    }

    private fun getMoviesLikedByUsers(userRatings: Map<Long, List<RealRating>>, usersMeanRatings: Map<Long, Double>): Map<Long, List<Long>> {
        return userRatings.map { userWithRatings ->
            Pair(userWithRatings.key,
                userWithRatings.value
                    .filter { it.rating > usersMeanRatings.getOrElse(userWithRatings.key, { 1.0 }) }
                    .map { it.movie })
        }.toMap()
    }

    private fun calculateGoodRecommendationsCount(moviesLikedByUsers: Map<Long, List<Long>>,
                                                  usersWithRatingsAndReco: List<Triple<Long, List<RealRating>, List<MovieRecommendation>>>): Map<Long, Int> {
        return usersWithRatingsAndReco.map { userRecord ->
            Pair(
                userRecord.first,
                userRecord.third.map { it.movieId }.count { moviesLikedByUsers[userRecord.first]!!.contains(it) })
        }.toMap()
    }

    private fun precAndRecallAtN(moviesLikedByUsers: Map<Long, List<Long>>, usersWithRatingsAndReco: List<Triple<Long, List<RealRating>, List<MovieRecommendation>>>, n: Int): Pair<Double, Double> {

        val recoMapByUser = usersWithRatingsAndReco.map { Pair(it.first, it.third.take(n)) }.toMap()

        val goodRecommendationsCountPerUser = usersWithRatingsAndReco.map { userRecord ->
            Pair(
                userRecord.first,
                userRecord.third.take(n).map { it.movieId }.count { moviesLikedByUsers[userRecord.first]!!.contains(it) })
        }.toMap()


        val precisionPerUser = recoMapByUser.map { goodRecommendationsCountPerUser.get(it.key)!!.toDouble() / recoMapByUser.get(it.key)!!.count() }

        val recallPerUser = recoMapByUser
            .map { Pair(it.key, goodRecommendationsCountPerUser.get(it.key)!!.toDouble() / moviesLikedByUsers.get(it.key)!!.count()) }
            .map { Pair(it.first, if (it.second.equals(Double.NaN)) 0.0 else it.second) }
        return Pair(precisionPerUser.average(), recallPerUser.map { it.second }.average())
    }

    override fun finish(): MetricsResult<Pair<Double, Double>> {
        val result = MetricsResult(
            Pair(precisionSupportAccumulator.precisionResults.average(), precisionSupportAccumulator.recallResults.average()),
            precisionSupportAccumulator.times.sum(),
            mapOf())
        precisionSupportAccumulator = DecisionSupportAccumulator()
        return result
    }
}