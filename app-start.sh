#!/bin/bash

# Running docker container on which the data will be stored
docker build -t postgres-custom .
docker run --name postgres-container -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=root -p 5432:5432 -d postgres-custom
sleep 6

# Then running workers on which the work on db will be done
#./gradlew runWorkers
sleep 3

# Then running load-balancer to serve the clients
#./gradlew runLoadBalancer
sleep 3

# And finally running the client to send the request
#./gradlew runClient
sleep 3
