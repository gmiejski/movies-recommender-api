package org.miejski.movies.recommender.ratings;

import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.miejski.movies.recommender.domain.recommendations.MovieRecommendation;
import org.miejski.movies.recommender.domain.recommendations.RecommendationsQuery;
import org.miejski.movies.recommender.domain.recommendations.RecommendationsServiceI;
import org.miejski.movies.recommender.neo4j.CypherExecutor;

import java.util.HashMap;
import java.util.List;

public class PredictionerService implements RecommendationsServiceI {

    private final CypherExecutor cypherExecutor;

    public PredictionerService(CypherExecutor cypherExecutor) {
        this.cypherExecutor = cypherExecutor;
    }

    @NotNull
    @Override
    public List<MovieRecommendation> findRecommendedMovies(long userId, double minSimilarity) {
        throw new NotImplementedException();
    }

    @Override
    public double predictedRating(long userId, long movieId) {

        String cypherQuery = new RecommendationsQuery().getPredictionQuery();

        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("movieId", movieId);

        cypherExecutor.execute(cypherQuery, params);

        return 0;
    }
}
