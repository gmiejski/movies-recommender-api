package org.miejski.movies.recommender.domain.recommendations

import org.miejski.movies.recommender.domain.queries.QueriesLoader
import org.springframework.stereotype.Component

@Component
class RecommendationsQuery : QueriesLoader() {

    fun getRecommendationQuery(): String {
        return loadCypherQuery("similiarity_neighbours_recommendation.cypher")
    }

    fun getPredictionQuery(): String {
        return loadCypherQuery("similarity_predicted_rating.cypher")
    }
}