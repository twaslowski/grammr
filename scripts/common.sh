function stop_environment() {
  echo "Not implemented yet."
  exit 1
}

function start_environment() {
  echo "Not implemented yet."
  exit 1
}

function package() {
  ./mvnw package -DskipTests
}

function build() {
  echo "Not implemented yet."
  exit 1

#  TAG=$1
#
#  if [ -z "$TAG" ]; then
#    echo "TAG is required"
#    exit 1
#  fi
#
#  ROOT=$(git rev-parse --show-toplevel)
#  package
#  docker build -t "open-mood-tracker:$TAG" "$ROOT"
}


function unit_test() {
  ./mvnw test
}

function integration_test() {
  ./mvnw package test -P integration
}

function benchmark() {
  ./mvnw package test -P benchmark
}

function run() {
  ./mvnw spring-boot:run
}