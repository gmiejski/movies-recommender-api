package org.miejski.movies.recommender.users

import org.miejski.movies.recommender.domain.Person
import org.miejski.movies.recommender.infrastructure.IntegrationSpec
import org.springframework.beans.factory.annotation.Autowired

class UsersServiceSpec extends IntegrationSpec {

    @Autowired
    UsersRepository usersRepository

    def "should save and load graph node from repository"() {
        given:
        usersRepository.save(new Person(null, 10L, []))
        def savedPerson2 = usersRepository.save(new Person(null, 10L, []))

        when:
        def loadedPerson = usersRepository.findOne(savedPerson2.id)

        then:
        print(usersRepository.findAll().size())
        usersRepository.findAll().size() == 2
        with(loadedPerson) {
            id == savedPerson2.id
            user_id == savedPerson2.user_id
        }
    }
}
