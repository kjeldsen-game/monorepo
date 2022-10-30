FROM amazoncorretto:17
EXPOSE 8080
ADD infrastructure/target/gateway-service.jar /myservice.jar
ENTRYPOINT ["java", "-jar", "/myservice.jar"]
