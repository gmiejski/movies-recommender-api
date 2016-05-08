package org.miejski.movies.recommender.recommendations

import org.miejski.movies.recommender.queries.Neo4jQueriesHolder
import org.springframework.data.neo4j.template.Neo4jOperations
import spock.lang.Specification
import spock.lang.Unroll

class RecommendationsServiceSpec extends Specification {

    RecommendationsService recommendationsService
    Neo4jOperations neo4jOperations

    void setup() {
        neo4jOperations = Mock(Neo4jOperations)
        recommendationsService = new RecommendationsService(neo4jOperations, new Neo4jQueriesHolder([:]))
    }

    @Unroll
    def "should calculate score for movie"() {
        expect:
        recommendationsService.calculateScore(prediction, sharedRatings, maxSharedRatings) == expectedScore

        where:
        prediction | sharedRatings | maxSharedRatings | expectedScore
        4.0        | 15            | 20               | 3.875649686842579.doubleValue()
        4.5        | 20            | 20               | 4.5.doubleValue()
    }
}
