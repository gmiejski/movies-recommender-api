package org.miejski.movies.recommender.domain.movie

import org.miejski.movies.recommender.domain.AbstractEntity

class Movie(

    id: Long? = null,

    var movie_id: Long = -1

) : AbstractEntity(id)