package org.miejski.movies.recommender.queries

import spock.lang.Specification

class Neo4jQueriesHolderFactorySpec extends Specification {

    def "should load cypher query"() {
        given:
        def cypherName = "some_query.cypher"

        when:
        def cypher = new Neo4jQueriesHolderFactory().loadCypherQuery(cypherName)

        then:
        cypher == "MATCH(n:Person) RETURN n LIMIT 10"
    }
}
