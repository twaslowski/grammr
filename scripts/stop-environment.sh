#!/usr/bin/env bash

PROJECT_ROOT=$(git rev-parse --show-toplevel)
export PROJECT_ROOT

source "$PROJECT_ROOT/scripts/common.sh"

stop_environment