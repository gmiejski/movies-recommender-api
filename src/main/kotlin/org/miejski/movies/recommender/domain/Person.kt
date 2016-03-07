package org.miejski.movies.recommender.domain

import org.neo4j.ogm.annotation.Relationship

class Person(id: Long? = null,
             var user_id: String = "",
             @Relationship(type = "Rated", direction = Relationship.OUTGOING)
             var ratedMovies: List<Movie> = emptyList()) : AbstractNode(id)