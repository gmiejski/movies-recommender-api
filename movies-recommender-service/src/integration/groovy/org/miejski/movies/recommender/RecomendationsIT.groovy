package org.miejski.movies.recommender

import org.miejski.movies.recommender.domain.recommendations.RecommendationsService
import org.miejski.movies.recommender.infrastructure.IntegrationSpec
import org.springframework.beans.factory.annotation.Autowired

class RecomendationsIT extends IntegrationSpec {


    @Autowired
    RecommendationsService recommendationsService

    def "should return proper recommendations with good predictions"() {
        given:

        when:
        def prediction = recommendationsService.predictedRating(1, 1)


        then:
        prediction == 1.0 as Double

    }
}
