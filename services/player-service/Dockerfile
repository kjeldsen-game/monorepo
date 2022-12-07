FROM amazoncorretto:17
EXPOSE 8082
ADD ./infrastructure/target/player-service.jar /myservice.jar
ENTRYPOINT ["java", "-jar", "/myservice.jar"]
