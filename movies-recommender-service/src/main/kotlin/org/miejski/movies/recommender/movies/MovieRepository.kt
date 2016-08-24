package org.miejski.movies.recommender.movies

import org.miejski.movies.recommender.domain.Movie
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository

open interface MovieRepository : GraphRepository<Movie> {

    @Query("MATCH (n:Movie) return n.movie_id")
    fun getAllIds(): List<Int>;

}