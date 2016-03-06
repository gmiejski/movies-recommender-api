package org.miejski.movies.recommender.domain;

import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

public class Person extends AbstractNode {

    @Relationship(type = "Rated", direction = Relationship.OUTGOING)
    private List<Movie> ratedMovies;

    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<Movie> getRatedMovies() {
        return ratedMovies;
    }
}
