package org.miejski.movies.recommender.users

import org.miejski.movies.recommender.domain.Person
import org.springframework.data.neo4j.repository.GraphRepository

interface  UsersRepository : GraphRepository<Person>{
}