function stop_environment() {
  docker compose -f local/docker-compose.yaml logs --timestamps analysis-de > target/docker.log
  docker compose -f local/docker-compose.yaml down
}

function start_environment() {
  docker compose -f local/docker-compose.yaml up -d --build --wait
}

function deploy() {
  export TAG="sha-$(git rev-parse --short HEAD)"
  export HELM_TIMEOUT=300s

  if [ -z "$OPENAI_API_KEY" ]; then
    echo "Please set OPENAI_API_KEY environment variable"
    exit 1
  fi

  helm upgrade --install \
    --values ./charts/values/postgres-values.yaml \
    --namespace grammr --create-namespace \
    --wait --timeout "$HELM_TIMEOUT" \
    postgres oci://registry-1.docker.io/bitnamicharts/postgresql

  helm upgrade --install \
    --set openai_api_key="$OPENAI_API_KEY" \
    --set global.image.tag="$TAG" \
    --values ./charts/values/grammr-values.yaml \
    --namespace grammr --create-namespace \
    --wait --timeout "$HELM_TIMEOUT" \
    grammr-core ./charts/grammr
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
