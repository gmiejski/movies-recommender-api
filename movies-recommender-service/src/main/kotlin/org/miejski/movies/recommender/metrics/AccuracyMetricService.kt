package org.miejski.movies.recommender.metrics

import com.codahale.metrics.MetricRegistry
import org.miejski.movies.recommender.recommendations.RecommendationsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.System.currentTimeMillis
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Callable
import java.util.concurrent.Executors

@Service
open class AccuracyMetricService @Autowired constructor(val recommendationsService: RecommendationsService,
                                                        val metricRegistry: MetricRegistry) {
    private val logger = LoggerFactory.getLogger(AccuracyMetricService::class.java)

    fun run(testFilePath: String?): Double {
        val allLines = Files.readAllLines(Paths.get(testFilePath))
        logger.warn("Looking for predictions for {} ratings.", allLines.size.toString())
        val newFixedThreadPool = Executors.newFixedThreadPool(15)
        val start = currentTimeMillis()

        val tasks = allLines.drop(1)
            .map { toRating(it) }
            .map {
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
        logger.warn("Found rating for {} movies", predictedRatings.size)
        logger.warn("Resulting rmse = {} from fold: {} in time: {} seconds", result.toString(), testFilePath, (stop - start) / 1000.0)
        return result
    }

    fun toRating(line: String): RealRating {
        val splittedLine = line.split("\t")
        return RealRating(splittedLine[0].toLong(), splittedLine[1].toLong(), splittedLine[2].toDouble(), splittedLine[3].toLong())
    }

    fun joinWithPrediction(realRating: RealRating): Pair<Double, Double> {
        return Pair(realRating.rating, recommendationsService.predictedRating(realRating.person, realRating.movie))
    }
}

data class RealRating(val person: Long, val movie: Long, val rating: Double, val timestamp: Long) {
}