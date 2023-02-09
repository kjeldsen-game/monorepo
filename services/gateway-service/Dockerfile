FROM amazoncorretto:17

ADD infrastructure/target/gateway-service.jar /myservice.jar

HEALTHCHECK --interval=20s --timeout=5s --retries=5 \
    CMD curl --fail --silent localhost:8080/actuator/health | grep UP || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/myservice.jar"]
