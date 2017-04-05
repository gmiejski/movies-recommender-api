package org.miejski.movies.recommender.state;

import org.jetbrains.annotations.NotNull;
import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStateAssertion;
import org.miejski.movies.recommender.neo4j.CypherExecutor;
import org.neo4j.driver.v1.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataImportedStateAssertion implements Neo4jStateAssertion {

    private final String trainingDataFile;
    private final CypherExecutor cypherExecutor;

    public DataImportedStateAssertion(String trainingDataFile, CypherExecutor cypherExecutor) {
        this.trainingDataFile = trainingDataFile;
//        this.trainingDataFile = "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_train_0";
        this.cypherExecutor = cypherExecutor;
    }

    @NotNull
    @Override
    public String name() {
        return "DataImportedStateAssertion";
    }

    @NotNull
    @Override
    public String queryToExecute() {
        return "start_state/import_data.cypher";
    }

    @Override
    public boolean isOK() {
//        String locadedQuery = new RecommendationsQuery().loadCypherQuery(queryToExecute());

        List<Record> list = cypherExecutor.execute("Match (p:Person) return p limit 1");

        return !list.isEmpty();
    }

    @NotNull
    @Override
    public Map<String, Object> queryToExecuteParams() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("trainingDataFile", "file://" + trainingDataFile + "");
        return stringObjectHashMap;
    }
}
