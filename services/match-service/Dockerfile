FROM amazoncorretto:17
EXPOSE 8083
ADD ./infrastructure/target/match-service.jar /myservice.jar
ENTRYPOINT ["java", "-jar", "/myservice.jar"]
