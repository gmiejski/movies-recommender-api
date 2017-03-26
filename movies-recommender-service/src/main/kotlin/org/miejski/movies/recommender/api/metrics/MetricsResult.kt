package org.miejski.movies.recommender.api.metrics

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MetricsResult<T> constructor(val result: T, val timeInSeconds: Double, val others: Map<String, Any>) {

}