package org.miejski.movies.recommender.users

import org.miejski.movies.recommender.domain.Person
import org.miejski.movies.recommender.infrastructure.Neo4jSpecification
import org.neo4j.ogm.session.Session
import org.springframework.beans.factory.annotation.Autowired

class UsersServiceTest extends Neo4jSpecification {

    @Autowired
    UsersRepository repository
    @Autowired
    Session session

    def "should return something from "() {
        given:
        repository.save(new Person(10L, 10L, []))

        when:
        def person = repository.findOne(10L)

        then:
        person != null
        person.user_id == 10
    }
}
