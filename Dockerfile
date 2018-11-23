FROM openjdk:8-jre-slim

RUN mkdir /app

WORKDIR /app

ADD ./api/target/bikeshare-catalogue-api-1.0.0-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
