package org.miejski.movies.recommender.domain.recommendations

import org.neo4j.ogm.session.Neo4jSession
import spock.lang.Specification
import spock.lang.Unroll

class RecommendationsServiceSpec extends Specification {

    Neo4jSession neo4jSession = Mock(Neo4jSession)
    RecommendationsService recommendationsService = new RecommendationsService(neo4jSession, new RecommendationsQuery())

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
