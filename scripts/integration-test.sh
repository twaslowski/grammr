#!/usr/bin/env zsh

./scripts/start-local-dependencies.sh
source .env

./mvnw test -P integration
./mvnw jacoco:report