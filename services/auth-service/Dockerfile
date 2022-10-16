FROM amazoncorretto:17
EXPOSE 8081
ADD infrastructure/target/myservice.jar /myservice.jar
ENTRYPOINT ["java", "-jar", "/myservice.jar"]
