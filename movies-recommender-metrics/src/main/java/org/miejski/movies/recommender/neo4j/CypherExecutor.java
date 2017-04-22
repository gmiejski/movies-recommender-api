package org.miejski.movies.recommender.neo4j;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CypherExecutor {

    private final Driver driver;

    public CypherExecutor() {
        driver = GraphDatabase.driver("bolt://localhost:7699", AuthTokens.basic("neo4j", "neo4j1234"));
    }


    public List<Record> execute(String query, Map<String, Object> params) {
        Session session = driver.session();

        StatementResult run = session.run(query, params);

        List<Record> resultList = run.list();
        session.close();

        return resultList;
    }

    public List<Record> execute(String query) {
        Map<String, Object> params = new HashMap<>();
        return execute(query, params);
    }

    public void close() {
        driver.close();
    }
}
