#!/usr/bin/env bash

set -eo pipefail

PROJECT_ROOT=$(git rev-parse --show-toplevel)
export PROJECT_ROOT
source "$PROJECT_ROOT/scripts/common.sh"

deploy