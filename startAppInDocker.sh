#!/usr/bin/env bash
# This scripts builds and starts
# 1. The official MongoDB 3.4.2 container
# 2. The application docker container and link to the MongoDB container,
# so the application can access the MongoDB container via mongodb
docker build -t lyashenkogs/analytics4github:0.0.2 .
docker stop $(docker ps -aq)

#docker inspect -f "{{.HostConfig.Links}}" analytics4github
docker run -it --name mongodb -p 27017:27017  -d  mongo:3.4.2 --auth
docker run  -p 8081:8080 --name analytics4github --link mongodb:mongo -d lyashenkogs/analytics4github:0.0.2