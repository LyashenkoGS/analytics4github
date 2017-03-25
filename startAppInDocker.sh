#!/usr/bin/env bash
# This scripts builds and starts
# 1. The official MongoDB 3.4.2 container
# 2. The application docker container and link to the MongoDB container,
# so the application can access the MongoDB container via mongodb
docker build -t lyashenkogs/analytics4github:0.0.2 .
docker stop $(docker ps -aq)
docker run --name mongodb -d mongo:3.4.2
docker run --name analytics4github -d -p 8081:8080  --link mongodb:mongodb -d lyashenkogs/analytics4github:0.0.2