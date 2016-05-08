package org.miejski.movies.recommender.infrastructure

import org.junit.rules.ExternalResource
import org.neo4j.ogm.testutil.TestServer

class Neo4jResource extends ExternalResource {

    TestServer server

    Neo4jResource(int port) {
        server = new TestServer(port)

        print(server.url())
    }

    @Override
    protected void before() throws Throwable {
    }

    @Override
    protected void after() {
        server.shutdown()
    }
}
