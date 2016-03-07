package org.miejski.movies.recommender.domain.relationships

import com.fasterxml.jackson.annotation.JsonIgnore
import org.miejski.movies.recommender.domain.Movie
import org.miejski.movies.recommender.domain.Person
import org.neo4j.ogm.annotation.*

@RelationshipEntity(type = "Rated")
class Rating {

    @GraphId
    @JvmField var id: Long? = null

    @StartNode
    @JsonIgnore
    @JvmField var person: Person? = null

    @EndNode
    @JvmField var movie: Movie? = null

    @Property
    @JvmField var rating: String = ""
}