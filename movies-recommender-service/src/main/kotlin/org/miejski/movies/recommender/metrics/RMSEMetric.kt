package org.miejski.movies.recommender.metrics

open class RMSEMetric {
    companion object {
        fun calculate(predictedRatings: List<Pair<Double, Double>>): Double {
            val mse = predictedRatings
                .map { (it.first - it.second) * (it.first - it.second) }
                .sum() / predictedRatings.size.toDouble()
            return Math.abs(mse)
        }
    }
}