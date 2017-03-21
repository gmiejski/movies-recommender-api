package org.miejski.movies.recommender.domain.rating

import com.fasterxml.jackson.annotation.JsonIgnore
import org.miejski.movies.recommender.domain.movie.Movie
import org.miejski.movies.recommender.domain.user.Person
import org.neo4j.ogm.annotation.*

@RelationshipEntity(type = "Rated")
class Rating(

    @GraphId
    var id: Long? = null,

    @StartNode
    @JsonIgnore
    var person: Person? = null,

    @EndNode
    var movie: Movie? = null,

    @Property
    var rating: Double? = null
)