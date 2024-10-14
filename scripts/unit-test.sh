#!/usr/bin/env zsh

./mvnw clean test
./mvnw jacoco:report