FROM amazoncorretto:17

ADD infrastructure/target/player-service.jar /myservice.jar

HEALTHCHECK --interval=20s --timeout=5s --retries=5 \
    CMD curl --fail --silent localhost:8082/actuator/health | grep UP || exit 1

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/myservice.jar"]
