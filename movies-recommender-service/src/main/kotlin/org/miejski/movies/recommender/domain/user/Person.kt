package org.miejski.movies.recommender.domain.user

import org.miejski.movies.recommender.domain.AbstractEntity
import org.miejski.movies.recommender.domain.rating.Rating
import org.neo4j.ogm.annotation.Relationship

class Person(

    id: Long? = null,

    var user_id: Long = -1,

    @Relationship(type = "Rated", direction = Relationship.OUTGOING)
    var ratedMovies: List<Rating> = emptyList(),
    var avg_rating: Double = -1.0
) : AbstractEntity(id)