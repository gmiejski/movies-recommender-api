package org.miejski.movies.recommender.metrics

import com.codahale.metrics.MetricRegistry
import org.miejski.movies.recommender.recommendations.RecommendationServiceI
import spock.lang.Specification

import static spock.util.matcher.HamcrestMatchers.closeTo

class AccuracyMetricServiceSpec extends Specification {

    def AccuracyMetricService accuracyMetricService
    def RecommendationServiceI recommendationsService

    void setup() {
        recommendationsService = Mock(RecommendationServiceI)
        accuracyMetricService = new AccuracyMetricService(recommendationsService, Mock(MetricRegistry))
    }

    def "should join 2 results and return a result"() {
        given:
        recommendationsService.predictedRating(1, 1) >> 4.0
        recommendationsService.predictedRating(2, 2) >> 4.5

        when: "run for the first time"
        accuracyMetricService.run([new RealRating(1, 1, 5.0, 1)])

        and: "run for the second time"
        accuracyMetricService.run([new RealRating(2, 2, 3.0, 1), new RealRating(2, 3, 3.0, 1)])

        and: "finished calculating rmse"
        def metricResult = accuracyMetricService.finish()

        then:
        with(metricResult) {
            result == 1.625
            closeTo(66.6, 1).matches(metricResult.others["percentageOfFoundRatings"])
        }
    }
}
