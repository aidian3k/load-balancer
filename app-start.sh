#!/bin/bash

# Running docker container on which the data will be stored
docker build -t postgres-custom .
docker run --name postgres-container -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=root -p 5432:5432 -d postgres-custom

# Then running workers on which the work on db will be done
./gradlew runWorkers

# Then running load-balancer to serve the clients
./gradlew runLoadBalancer

# And finally running the client to send the request
./gradlew runClient
