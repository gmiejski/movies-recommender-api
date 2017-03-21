package org.miejski.movies.recommender.domain.metrics

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RunMetricsRequest constructor(val testFilePath: String?, val testName: String?) {
    constructor() : this(null, null)
}