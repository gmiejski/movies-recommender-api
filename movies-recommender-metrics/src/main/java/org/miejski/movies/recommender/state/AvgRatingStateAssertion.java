package org.miejski.movies.recommender.state;

import org.jetbrains.annotations.NotNull;
import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStateAssertion;
import org.miejski.movies.recommender.neo4j.CypherExecutor;

import java.util.HashMap;
import java.util.Map;

public class AvgRatingStateAssertion implements Neo4jStateAssertion {

    private final CypherExecutor cypherExecutor;

    public AvgRatingStateAssertion(CypherExecutor cypherExecutor) {
        this.cypherExecutor = cypherExecutor;
    }

    @NotNull
    @Override
    public String name() {
        return "AvgRatingStateAssertion";
    }

    @NotNull
    @Override
    public String queryToExecute() {
        return "start_state/average_rating.cypher";
    }

    @NotNull
    @Override
    public Map<String, Object> queryToExecuteParams() {
        return new HashMap<>();
    }

    @Override
    public boolean isOK() {
        return !cypherExecutor.execute("Match (p:Person) where exists(p.avg_rating) return p limit 1").isEmpty();
    }
}
