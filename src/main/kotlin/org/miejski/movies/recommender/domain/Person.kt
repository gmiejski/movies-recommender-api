package org.miejski.movies.recommender.domain

import org.miejski.movies.recommender.domain.relationships.Rating
import org.neo4j.ogm.annotation.Relationship

class Person(

    id: Long? = null,

    var user_id: Long = -1,

    @Relationship(type = "Rated", direction = Relationship.OUTGOING)
    var ratedMovies: List<Rating> = emptyList()

) : AbstractEntity(id)