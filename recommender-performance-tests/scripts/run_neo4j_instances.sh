#!/usr/bin/env bash

aws ec2 run-instances --image-id ami-db59afb4 --count 1 --instance-type t2.micro --key-name movies-recommender-service --security-groups movies-recommender-service-sg
aws ec2 create-tags --resources i-64266ed8 --tags Key=service-name,Value=movies-recommender-neo4j
aws ec2 terminate-instances --instance-ids i-64266ed8