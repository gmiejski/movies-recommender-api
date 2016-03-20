package org.miejski.movies.recommender.users

import org.miejski.movies.recommender.domain.Person
import org.miejski.movies.recommender.infrastructure.Neo4jSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Transactional
class UsersServiceTest extends Neo4jSpecification {

    @Autowired
    UsersRepository usersRepository

    def "should return something from "() {
        given:
        create(new Person(10L, 10L, []))

        when:
        def person = usersRepository.findOne(10L)

        then:
        print(usersRepository.findAll().size())
        usersRepository.findAll().size() == 1
        person != null
        person.user_id == 10
    }

    @Transactional
    def create(Person person) {
        usersRepository.save(person)
    }
}
