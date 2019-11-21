# Building stage
FROM maven:3.6.2-jdk-11-slim AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY ./src ./src
RUN mvn clean package

# Running stage
FROM openjdk:11.0.5-jdk-slim-buster
COPY --from=builder /build/target/promo-1.0.jar .
ENTRYPOINT java -jar promo-1.0.jar