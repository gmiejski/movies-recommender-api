package org.miejski.movies.recommender.domain

import org.miejski.movies.recommender.domain.relationships.Rating
import org.neo4j.ogm.annotation.Relationship

class Person(id: Long? = null) : AbstractEntity(id) {

    @JvmField var user_id: String = ""

    @Relationship(type = "Rated", direction = Relationship.OUTGOING)
    @JvmField var ratedMovies: List<Rating> = emptyList()

}