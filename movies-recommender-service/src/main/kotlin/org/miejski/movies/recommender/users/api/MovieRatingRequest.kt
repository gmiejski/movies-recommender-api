package org.miejski.movies.recommender.users.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MovieRatingRequest(var movieId: String? = null, var rating: Double? = null)