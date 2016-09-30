package org.miejski.movies.recommender.metrics.decisionSupport

import java.util.*

class DecisionSupportAccumulator() {

    val times = ArrayList<Double>()
    val precisionResults = ArrayList<Double>()
    val recallResults = ArrayList<Double>()

    fun saveResult(precisionResult: Double, recallResult: Double, timeInSeconds: Double) {
        precisionResults.add(precisionResult)
        recallResults.add(recallResult)
        times.add(timeInSeconds)
    }
}