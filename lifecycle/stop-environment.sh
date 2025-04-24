#!/usr/bin/env bash

PROJECT_ROOT=$(git rev-parse --show-toplevel)
export PROJECT_ROOT

source "$PROJECT_ROOT/lifecycle/common.sh"

stop_environment