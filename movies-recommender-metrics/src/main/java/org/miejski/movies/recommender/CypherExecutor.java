package org.miejski.movies.recommender;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class CypherExecutor {

    public void execute() {
        Driver driver = GraphDatabase.driver("bolt://localhost:7699", AuthTokens.basic("neo4j", "neo4j1234"));
        Session session = driver.session();

        StatementResult result = session.run("MATCH (a:Person) RETURN a.user_id limit 100 ");

//        session.run( "CREATE (a:Person {name: {name}, title: {title}})",
//                parameters( "name", "Arthur", "title", "King" ) );
//
//        StatementResult result = session.run( "MATCH (a:Person) WHERE a.name = {name} RETURN a.name AS name, a.title AS title", parameters("name", "Arthur" ) );
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println(record.get("a.user_id").asInt());
        }

        session.close();
        driver.close();
    }
}
