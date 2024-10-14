#!/usr/bin/env zsh

trap stop SIGINT EXIT SIGTERM

function stop() {
  echo "Stopping ..."
  unset SPRING_PROFILES_ACTIVE
  scripts/stop-local-dependencies.sh
}

source ".env"
export SPRING_PROFILES_ACTIVE=local

scripts/start-local-dependencies.sh

./mvnw clean spring-boot:run
