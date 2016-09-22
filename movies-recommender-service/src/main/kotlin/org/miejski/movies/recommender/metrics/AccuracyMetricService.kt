package org.miejski.movies.recommender.metrics

import org.miejski.movies.recommender.recommendations.RecommendationsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
open class AccuracyMetricService(@Autowired val recommendationsService: RecommendationsService) {
    fun run(testFilePath: String?) {
        val allLines = Files.readAllLines(Paths.get(testFilePath))

        val predictedRatings = allLines.drop(1)
            .map { toRating(it) }
            .map { joinWithPrediction(it) }

    }

    fun toRating(line: String): RealRating {
        val splittedLine = line.split("\t")
        return RealRating(splittedLine[0], splittedLine[1], splittedLine[2].toDouble(), splittedLine[3].toLong())
    }

    fun joinWithPrediction(realRating: RealRating): Pair<Double, Double> {
        return Pair(realRating.rating, recommendationsService.predictedRating(realRating.person, realRating.movie))
    }
}

data class RealRating(val person: String, val movie: String, val rating: Double, val timestamp: Long) {
}