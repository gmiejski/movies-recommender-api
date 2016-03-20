package org.miejski.movies.recommender.movies

import org.miejski.movies.recommender.domain.Movie
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
interface MovieRepository : GraphRepository<Movie> {
}