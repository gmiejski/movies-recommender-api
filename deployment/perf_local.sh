#!/usr/bin/env bash

sim_methods=(cosine pearson_with_sw)
top_n=(10 15 20 25 30 35 40 45 50 100)
for sim_method in ${sim_methods[*]}; do
    for n in ${top_n[*]}; do
        ./gradlew  loadTest -Psimulation=org.miejski.movies.recommender.performance.RecommendationsSimulation -PapplicationUrl=http://localhost:8080 -Pmin_similarity=0.0 -Psimilarity_method=${sim_method} -PneighboursCount=${n} >> /tmp/magisterka/perf/${sim_method}_${n}
    done
done