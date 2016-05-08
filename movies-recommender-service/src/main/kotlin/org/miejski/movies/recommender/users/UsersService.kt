package org.miejski.movies.recommender.users

import org.miejski.movies.recommender.domain.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsersService @Autowired constructor(val usersRepository: UsersRepository) {
    fun findUser(userId: Long): Person {
        return usersRepository.findOne(userId)
    }

    fun findAll(): List<Person> = usersRepository.findAll().toList()

    fun findAllIds(): List<Int> = usersRepository.getAllIds()
}