package org.miejski.movies.recommender.users.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MovieRatingRequest constructor(val movieId: Long? = null, val rating: Double? = null) {
    constructor() : this(null, null)
}