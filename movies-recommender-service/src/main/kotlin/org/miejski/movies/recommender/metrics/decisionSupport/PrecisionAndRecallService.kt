package org.miejski.movies.recommender.metrics.decisionSupport

import org.miejski.movies.recommender.metrics.MetricsService
import org.miejski.movies.recommender.metrics.accuracy.RealRating
import org.miejski.movies.recommender.recommendations.RecommendationsServiceI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class PrecisionAndRecallService @Autowired constructor(val recommendationsService: RecommendationsServiceI)
: MetricsService() {
    override fun run(realRatings: List<RealRating>): Double {
        realRatings.groupBy { it.person }
            .map { Pair(it.value, recommendationsService.findRecommendedMovies(it.key)) }

        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}