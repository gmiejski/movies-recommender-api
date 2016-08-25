package org.miejski.movies.recommender.users

import org.miejski.movies.recommender.domain.Person
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface UsersRepository : GraphRepository<Person> {

    @Query("MATCH (n:Person) return n.user_id")
    fun getAllIds(): List<Int>;

    @Query("MATCH (n:Person {user_id: {userId}}),(m:Movie {movie_id: {movieId}}) merge (n)-[r:Rated]-(m) SET r.rating={ratingValue} return n")
    fun addMovieRating(@Param("userId") userId: Long, @Param("ratingValue") rating: Double, @Param("movieId") movieId: Long)
}