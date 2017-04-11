package org.miejski.movies.recommender.state;

import org.jetbrains.annotations.NotNull;
import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStateAssertion;
import org.miejski.movies.recommender.neo4j.CypherExecutor;
import org.neo4j.driver.v1.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Similarity1StateAssertion implements Neo4jStateAssertion {

    private final CypherExecutor cypherExecutor;

    public Similarity1StateAssertion(CypherExecutor cypherExecutor) {
        this.cypherExecutor = cypherExecutor;
    }

    @NotNull
    @Override
    public String name() {
        return "Similarity1StateAssertion";
    }

    @NotNull
    @Override
    public String queryToExecute() {
        return "start_state/similarity_1.cypher";
    }

    @NotNull
    @Override
    public Map<String, Object> queryToExecuteParams() {
        return new HashMap<>();
    }

    @Override
    public boolean isOK() {
        List<Record> list = cypherExecutor.execute("MATCH (p:Person)-[s:Similarity]->(p2:Person) return s limit 1");
        return !list.isEmpty();
    }
}
