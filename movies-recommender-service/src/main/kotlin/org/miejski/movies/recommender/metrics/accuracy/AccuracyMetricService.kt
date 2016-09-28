package org.miejski.movies.recommender.metrics.accuracy

import org.miejski.movies.recommender.metrics.MetricsResult
import org.miejski.movies.recommender.metrics.MetricsService
import org.miejski.movies.recommender.recommendations.RecommendationsServiceI
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.System.currentTimeMillis

@Service
open class AccuracyMetricService @Autowired constructor(val recommendationsService: RecommendationsServiceI) : MetricsService() {
    private val logger = LoggerFactory.getLogger(AccuracyMetricService::class.java)
    private var accuracyAccumulator = AccuracyAccumulator()

    override fun run(realRatings: List<RealRating>): Double {
        logger.info("Looking for predictions for {} ratings.", realRatings.size.toString())
        val start = currentTimeMillis()

        val predictedRatings = runAsyncAndGather(realRatings, { joinWithPredictedRating(it) })
            .filter { it.second > 0 }

        val result = RMSEMetric.calculate(predictedRatings)
        val stop = currentTimeMillis()
        val timeInSeconds = (stop - start) / 1000.0

        accuracyAccumulator.saveResult(result, timeInSeconds, predictedRatings.size, realRatings.size)

        logger.info("Found rating for {} movies", predictedRatings.size)
        logger.info("Resulting rmse = {} in time: {} seconds", result.toString(), timeInSeconds)
        return result
    }

    fun joinWithPredictedRating(it: RealRating): () -> Pair<Double, Double> {
        return { Pair(it.rating, recommendationsService.predictedRating(it.person, it.movie)) }
    }

    fun finish(): MetricsResult<Double> {
        val result = MetricsResult(accuracyAccumulator.results.average(),
            accuracyAccumulator.times.sum(),
            mapOf(Pair("percentageOfFoundRatings", accuracyAccumulator.foundRatingsCounts.sum().toDouble() / accuracyAccumulator.orderedPredictiosCounts.sum().toDouble() * 100.0)))
        accuracyAccumulator = AccuracyAccumulator()
        return result
    }
}

data class RealRating(val person: Long, val movie: Long, val rating: Double, val timestamp: Long) {}