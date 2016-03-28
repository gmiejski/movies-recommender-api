package org.miejski.movies.recommender.users

import org.miejski.movies.recommender.domain.Person
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository

interface UsersRepository : GraphRepository<Person> {

    @Query("MATCH (n:Person) return n.user_id")
    fun getAllIds(): List<Int>;

}