FROM amazoncorretto:17

ADD infrastructure/target/match-service.jar /myservice.jar

HEALTHCHECK --interval=20s --timeout=5s --retries=5 \
    CMD curl --fail --silent localhost:8083/actuator/health | grep UP || exit 1

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/myservice.jar"]