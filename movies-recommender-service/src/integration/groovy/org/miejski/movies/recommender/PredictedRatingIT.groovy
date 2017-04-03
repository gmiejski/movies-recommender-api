package org.miejski.movies.recommender

import org.miejski.movies.recommender.domain.recommendations.RecommendationsService
import org.miejski.movies.recommender.infrastructure.IntegrationSpec
import org.springframework.beans.factory.annotation.Autowired

class PredictedRatingIT extends IntegrationSpec {

    @Autowired
    RecommendationsService recommendationsService

    def "should return predicted rating"() {
        given:
        long userUnderTest = 1
        long movieUnderTest = 5
        dbSetup.usersExists(userUnderTest, 2, 0.8)
        dbSetup.usersExists(userUnderTest, 3, 0.7)
        dbSetup.usersExists(userUnderTest, 4, 0.5)
        dbSetup.usersExists(userUnderTest, 5, -0.2)
        dbSetup.averageRatings {
            forUser(2, 3.5)
            forUser(3, 4.5)
            forUser(4, 3.8)
            forUser(5, 2.0)
        }
        dbSetup.ratings {
            rating(2, movieUnderTest, 5)
            rating(3, movieUnderTest, 5)
            rating(4, movieUnderTest, 3)
            rating(5, movieUnderTest, 2)
        }
        dbSetup.test()

        when:
        def rating = recommendationsService.predictedRating(userUnderTest, movieUnderTest)

        then:
        rating == 4.475 as Double
    }
}
