ARG MAVEN_IMAGE=maven:3.8.5-eclipse-temurin-17
ARG JRE_IMAGE=amazoncorretto:17

# Stage 1: Buils the application using Maven
FROM ${MAVEN_IMAGE} AS builder
WORKDIR /app/src
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn clean install -DskipTests=true -U

# Stage 2: Run the application using JRE
FROM $JRE_IMAGE
COPY --from=builder /app/src/infrastructure/target/*-service.jar /app/service.jar
ENTRYPOINT ["java", "-Xms100M", "-Xmx1G", "-jar", "/app/service.jar"]
