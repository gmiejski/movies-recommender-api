#!/usr/bin/env bash

# copy dataset to remote:
scp -r -i /Users/grzegorz.miejski/.ssh/movies-recommender-service.pem ml-100k/ ec2-user@52.29.124.164:/home/ec2-user/programming/neo4j-community-3.0.4/data/databases/