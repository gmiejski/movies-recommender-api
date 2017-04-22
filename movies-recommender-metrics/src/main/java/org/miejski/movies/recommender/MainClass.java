package org.miejski.movies.recommender;

import org.miejski.movies.recommender.api.metrics.MetricsResult;
import org.miejski.movies.recommender.domain.metrics.accuracy.AccuracyMetricService;
import org.miejski.movies.recommender.infrastructure.dbstate.Neo4jStarStateAsserter;
import org.miejski.movies.recommender.infrastructure.dbstate.assertions.MovieIndexAssertion;
import org.miejski.movies.recommender.infrastructure.dbstate.assertions.PersonIndexAssertion;
import org.miejski.movies.recommender.neo4j.CypherExecutor;
import org.miejski.movies.recommender.ratings.PredictionerService;
import org.miejski.movies.recommender.state.AvgRatingStateAssertion;
import org.miejski.movies.recommender.state.DataImportedStateAssertion;
import org.miejski.movies.recommender.state.Similarity1StateAssertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) throws IOException {
        args = new String[]{
                "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_train_0",
                "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_test_0"};

        Logger logger = LoggerFactory.getLogger(MainClass.class);
        logger.info("START");

        if (args.length != 0) {
            logger.info("no args! quit");
        }
        String trainDataPath = args[0];
        String testDataPath = args[1];

        final CypherExecutor cypherExecutor = new CypherExecutor();
        logger.info("START - state assertions");
        new Neo4jStarStateAsserter((cypher, queryToExecuteParams) -> {
            HashMap<String, Object> stringHashMap = new HashMap<>(queryToExecuteParams);
            cypherExecutor.execute(cypher, stringHashMap);
        },
                () -> Arrays.asList(
                        new PersonIndexAssertion(),
                        new MovieIndexAssertion(),
                        new DataImportedStateAssertion(trainDataPath, cypherExecutor),
                        new AvgRatingStateAssertion(cypherExecutor),
                        new Similarity1StateAssertion(cypherExecutor)))
                .run();

        logger.info("START - metric calculation");
        AccuracyMetricService accuracyMetricService = new AccuracyMetricService(new PredictionerService(cypherExecutor));
        accuracyMetricService.run(testDataPath);
        MetricsResult<Double> finish = accuracyMetricService.finish();
        logger.info(finish.getResult().toString());
        logger.info(Arrays.toString(args));
//        Stream<Path> list = Files.list(Paths.get(args[0]));
//        list.forEach(System.out::println);


        cypherExecutor.close();
        logger.info("END");
    }
}
