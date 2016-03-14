package org.miejski.movies.recommender;

import org.neo4j.ogm.testutil.TestServer;
import org.springframework.data.neo4j.server.Neo4jServer;

/**
 * @author Michal Bachman
 * @author Luanne Misquitta
 */
public class InProcessServer implements Neo4jServer {

    private TestServer testServer;

    public InProcessServer()  {
        this.testServer = new TestServer();
    }

    public String url() {
        return testServer.url();
    }

    @Override
    public String username() {
        return null;
    }

    @Override
    public String password() {
        return null;
    }
}