# bikeshare-catalogue

## Running the microservice

Create docker network `bikeshare`:

```bash
docker network create bikeshare
```

Start the database with `docker-compose`:

```bash
docker-compose up
```

Build the image and start the microservice:

```bash
mvn clean package
docker build -t bikeshare-catalogue:snapshot .
docker run  --name bikeshare-catalogue \
            -p 8081:8080 \
            --network bikeshare \
            -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://bikeshare-catalogue.db.bikeshare:5432/bikeshare-catalogue \
            --rm \
            bikeshare-catalogue:snapshot
```
