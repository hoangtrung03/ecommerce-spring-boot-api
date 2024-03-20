FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean install

FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY target/Ecommerce-0.0.1-SNAPSHOT.jar app-1.0.0.jar
EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app-1.0.0.jar" ]
