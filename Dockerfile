FROM postgres:latest

# Copying the data into the container and setting password and user
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=root
COPY init.sql /docker-entrypoint-initdb.d/