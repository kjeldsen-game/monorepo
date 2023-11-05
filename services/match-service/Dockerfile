FROM amazoncorretto:17
EXPOSE 8083
ADD target/kjeldsen-match-*-SNAPSHOT.jar  /match-service.jar
ENTRYPOINT ["java", "-jar", "/match-service.jar"]
