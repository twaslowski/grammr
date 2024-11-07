FROM amazoncorretto:21-alpine

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --no-verbose -q --tries=1 --no-check-certificate https://localhost:8443/actuator/health || exit 1

ENV SPRING_PROFILES_ACTIVE=prod
ENV APP_DIR=/var/opt

RUN mkdir -p $APP_DIR

COPY target/grammr-core-1.0-SNAPSHOT.jar ${APP_DIR}/grammr-core.jar

EXPOSE 8443

CMD ["java", "-jar", "/var/opt/mood-tracker.jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"]