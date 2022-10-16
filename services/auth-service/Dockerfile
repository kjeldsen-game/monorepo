FROM amazoncorretto:17
ENTRYPOINT ["/usr/local/openjdk-17/bin/java", "-jar", "/usr/share/myservice/myservice.jar"]
EXPOSE 8081
ADD infrastructure/target/myservice.jar /usr/share/myservice/myservice.jar
