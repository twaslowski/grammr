function stop_environment() {
  docker compose -f local/docker-compose.yaml logs --timestamps analysis > target/docker.log
  docker compose -f local/docker-compose.yaml down
}

function start_environment() {
  docker compose -f local/docker-compose.yaml up -d --build --wait
}

function package() {
  ./mvnw package -DskipTests
}

function deploy() {

  export TAG="sha-$(git rev-parse --short HEAD)"

  if [ -z "$OPENAI_API_KEY" ] || [ -z "$TELEGRAM_TOKEN" ]; then
    echo "Please set OPENAI_API_KEY and TELEGRAM_TOKEN environment variables"
    exit 1
  fi

  helm upgrade --install \
    --values ./charts/values/postgres-values.yaml \
    --namespace grammr --create-namespace \
    --wait --timeout 300s \
    postgres oci://registry-1.docker.io/bitnamicharts/postgresql

  helm upgrade --install \
    --set image.tag=latest \
    --set spacyModels=ru_core_news_sm \
    --set image.tag="$TAG" \
    --namespace grammr --create-namespace \
    --wait --timeout 300s \
    grammr-morphology ./charts/sidecar-analysis

    helm upgrade --install \
      --set openai_api_key="$OPENAI_API_KEY" \
      --set telegram_token="$TELEGRAM_TOKEN" \
      --set image.tag="$TAG" \
      --namespace grammr --create-namespace \
      grammr-core ./charts/grammr
}

function activate_minikube() {
  minikube start
  eval "$(minikube docker-env)"
}

function unit_test() {
  ./mvnw package test
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
