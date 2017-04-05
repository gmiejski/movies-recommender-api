package org.miejski.movies.recommender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class MainClass {
    public static void main(String[] args) throws IOException {
        System.out.println("START");
        new CypherExecutor().execute();
        if (args.length > 0) {
            System.out.println(Arrays.toString(args));
            Stream<Path> list = Files.list(Paths.get(args[0]));
            list.forEach(System.out::println);
        }

        System.out.println("END");
    }
}
