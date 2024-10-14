#!/usr/bin/env zsh

./mvnw clean test

./scripts/start-local-dependencies.sh
./mvnw test -P integration
./scripts/stop-local-dependencies.sh

./mvnw jacoco:report