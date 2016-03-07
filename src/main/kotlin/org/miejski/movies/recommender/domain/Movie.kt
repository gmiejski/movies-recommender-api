package org.miejski.movies.recommender.domain

class Movie(id: Long? = null) : AbstractEntity(id) {

    @JvmField var movie_id: String = ""

}