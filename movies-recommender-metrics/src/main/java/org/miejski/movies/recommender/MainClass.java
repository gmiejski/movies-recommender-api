package org.miejski.movies.recommender;

import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws IOException {
        System.out.println("START");
        new CypherExecutor().execute();
//        System.out.println(Arrays.toString(args));
//        Files.readAllLines(Paths.get(args[0]));

        System.out.println("END");
    }
}
