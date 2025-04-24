#!/usr/bin/env bash

set -eo pipefail

PROJECT_ROOT=$(git rev-parse --show-toplevel)
export PROJECT_ROOT
source "$PROJECT_ROOT/lifecycle/common.sh"

# trap stop_environment SIGINT EXIT SIGTERM

export SPRING_PROFILES_ACTIVE=local

start_environment
run