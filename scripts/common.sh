function stop_environment() {
  docker compose -f local/docker-compose.yaml down
  exit 1
}

function start_environment() {
  docker compose -f local/docker-compose.yaml up -d --build analysis
  exit 1
}

function package() {
  ./mvnw package -DskipTests
}

function deploy() {
  TAG="sha-$(git rev-parse --short HEAD)"

  export TF_VAR_image_tag="$TAG"
  export TF_VAR_telegram_token="$TELEGRAM_TOKEN"

  pushd terraform || exit
  terraform init
  terraform apply -auto-approve
}

function activate_minikube() {
  minikube start
  eval "$(minikube docker-env)"
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