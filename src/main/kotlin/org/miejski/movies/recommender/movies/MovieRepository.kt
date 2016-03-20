package org.miejski.movies.recommender.movies

import org.miejski.movies.recommender.domain.Movie
import org.springframework.data.neo4j.repository.GraphRepository

interface MovieRepository : GraphRepository<Movie> {
}