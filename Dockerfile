FROM amazoncorretto:25-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

ENV APP_DIR=/var/opt

COPY --chown=appuser:appgroup target/grammr-core-1.0-SNAPSHOT.jar ${APP_DIR}/grammr-core.jar

EXPOSE 8443

USER appuser
WORKDIR $APP_DIR

CMD ["java", "-jar", "grammr-core.jar"]