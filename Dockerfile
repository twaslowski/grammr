FROM amazoncorretto:25-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

ENV APP_DIR=/var/opt
WORKDIR $APP_DIR

COPY --chown=appuser:appgroup target/grammr-core-1.0-SNAPSHOT.jar ${APP_DIR}/grammr-core.jar

USER appuser

EXPOSE 8443

CMD ["java", "-jar", "${APP_DIR}/grammr-core.jar"]