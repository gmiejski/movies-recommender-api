package org.miejski.movies.recommender.metrics

import java.util.*

class AccuracyAccumulator() {

    val times = ArrayList<Double>()
    val results = ArrayList<Double>()
    val foundRatingsCounts = ArrayList<Int>()
    val orderedPredictiosCounts = ArrayList<Int>()

    fun saveResult(result: Double, timeInSeconds: Double, predictedRatings: Int, orderedRatings: Int) {
        results.add(result)
        times.add(timeInSeconds)
        foundRatingsCounts.add(predictedRatings)
        orderedPredictiosCounts.add(orderedRatings)
    }
}