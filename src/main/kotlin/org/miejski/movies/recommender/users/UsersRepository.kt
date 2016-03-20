package org.miejski.movies.recommender.users

import org.miejski.movies.recommender.domain.Person
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
interface UsersRepository : GraphRepository<Person> {
}