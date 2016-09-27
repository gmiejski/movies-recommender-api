package org.miejski.movies.recommender.metrics

import org.miejski.movies.recommender.recommendations.RecommendationsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
open class AccuracyMetricService(@Autowired val recommendationsService: RecommendationsService) {
    fun run(testFilePath: String?): Double {
        val allLines = Files.readAllLines(Paths.get(testFilePath))

        val predictedRatings = allLines.drop(1)
            .map { toRating(it) }
            .map { joinWithPrediction(it) }
            .filter { it.second > 0 }

        val result = RMSEMetric.calculate(predictedRatings)
        println(result)
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