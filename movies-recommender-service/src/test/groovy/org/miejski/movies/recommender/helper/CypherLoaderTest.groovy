package org.miejski.movies.recommender.helper

import spock.lang.Specification

class CypherLoaderTest extends Specification {

    def "should load cypher query"() {
        given:
        def cypherName = "some_query.cypher"

        when:
        def cypher = new CypherLoader().loadCypherQuery(cypherName)

        then:
        cypher == "MATCH(n:Person) RETURN n LIMIT 10"
    }
}
