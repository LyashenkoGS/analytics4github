#!/usr/bin/env bash
#Builds and redeploys the application and a MongoDB in docker containers

#1. Build the application docker image
docker build -t lyashenkogs/analytics4github:0.0.2 .
#2. Try to stop and remove the existing MongoDB  and the application containers, if they exists
    ! docker stop analytics4github
    ! docker rm analytics4github
    ! docker stop mongodb
    ! docker rm mongodb
#2. Start the official MongoDB 3.4.2 container https://hub.docker.com/_/mongo/
# * no access restrictions (no login and password required)
# * map 21017 container's port to 27017 host port
docker run -it --name mongodb -p 27017:27017  -d  mongo:3.4.2
# 2. Run the application docker container with application-docker.properties
# * map 8080 container's port to 8081 host port
# * link  the MongoDB container as "mongo" to the application container
docker run  -p 8081:8080 --name analytics4github --link mongodb:mongo --env JAVA_OPTS=-Dspring.profiles.active=docker lyashenkogs/analytics4github:0.0.2