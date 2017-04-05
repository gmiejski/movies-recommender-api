package org.miejski.movies.recommender;

import org.miejski.movies.recommender.domain.metrics.accuracy.RealRating;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestDataReader {

    public Stream<RealRating> readFile(String testDataFile) throws IOException {
        return Files.lines(Paths.get(testDataFile)).map(this::toPredictedRating);
    }

    private RealRating toPredictedRating(String line) {
        String[] attrs = line.split("\t");
        long userId = Long.valueOf(attrs[0]);
        long movieId = Long.valueOf(attrs[1]);
        double rating = Double.valueOf(attrs[2]);
        return new RealRating(userId, movieId, rating, 0);
    }
}
