package org.miejski.movies.recommender.domain.metrics.rank

import org.miejski.movies.recommender.domain.metrics.MetricsResult
import org.miejski.movies.recommender.domain.metrics.MetricsService
import org.miejski.movies.recommender.domain.metrics.accuracy.RealRating
import org.miejski.movies.recommender.domain.recommendations.RecommendationsServiceI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class RankMetricService @Autowired constructor(val recommendationsService: RecommendationsServiceI) : MetricsService<Double>() {
    override fun finish(): MetricsResult<Double> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun run(realRatings: List<RealRating>): Double {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}