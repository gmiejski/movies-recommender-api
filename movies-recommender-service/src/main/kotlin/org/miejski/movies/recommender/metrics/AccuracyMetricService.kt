package org.miejski.movies.recommender.metrics

import com.codahale.metrics.MetricRegistry
import org.miejski.movies.recommender.recommendations.RecommendationServiceI
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.System.currentTimeMillis
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Callable
import java.util.concurrent.Executors

@Service
open class AccuracyMetricService @Autowired constructor(val recommendationsService: RecommendationServiceI,
                                                        val metricRegistry: MetricRegistry) {
    private val logger = LoggerFactory.getLogger(AccuracyMetricService::class.java)
    private var accuracyAccumulator = AccuracyAccumulator()

    fun run(testFilePath: String?): Double {
        val allLines = Files.readAllLines(Paths.get(testFilePath))
        logger.warn("Looking for predictions for {} ratings.", allLines.size.toString())

        val tasks = allLines.drop(1)
            .map { toRating(it) }
        return run(tasks)
    }

    private fun run(realRatings: List<RealRating>): Double {
        val newFixedThreadPool = Executors.newFixedThreadPool(15)
        val start = currentTimeMillis()

        val tasks = realRatings.map {
            Callable<Pair<Double, Double>>({
                Pair(it.rating, recommendationsService.predictedRating(it.person, it.movie))
            })
        }

        val predictions = newFixedThreadPool.invokeAll(tasks)
        newFixedThreadPool.shutdown()

        val predictedRatings = predictions.map { it.get() }
            .filter { it.second > 0 }

        val result = RMSEMetric.calculate(predictedRatings)
        val stop = currentTimeMillis()
        val timeInSeconds = (stop - start) / 1000.0

        accuracyAccumulator.saveResult(result, timeInSeconds, predictedRatings.size, tasks.size)

        logger.warn("Found rating for {} movies", predictedRatings.size)
        logger.warn("Resulting rmse = {} in time: {} seconds", result.toString(), timeInSeconds)
        return result
    }

    fun toRating(line: String): RealRating {
        val splittedLine = line.split("\t")
        return RealRating(splittedLine[0].toLong(), splittedLine[1].toLong(), splittedLine[2].toDouble(), splittedLine[3].toLong())
    }

    fun finish(): MetricsResult<Double> {
        val result = MetricsResult(accuracyAccumulator.results.average(),
            accuracyAccumulator.times.sum(),
            mapOf(Pair("percentageOfFoundRatings", accuracyAccumulator.foundRatingsCounts.sum().toDouble() / accuracyAccumulator.orderedPredictiosCounts.sum().toDouble() * 100.0)))

        accuracyAccumulator = AccuracyAccumulator()
        return result
    }

}

data class RealRating(val person: Long, val movie: Long, val rating: Double, val timestamp: Long) {
}