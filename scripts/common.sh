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
  if [ -z "$TAG" ]; then
    TAG="sha-$(git rev-parse --short HEAD)"
  fi

  if [ -z "$OPENAI_API_KEY" ] || [ -z "$DATASOURCE_PASSWORD" ] || [ -z "$TELEGRAM_TOKEN_PROD" ]; then
    echo "Please set OPENAI_API_KEY, DATASOURCE_PASSWORD and TELEGRAM_TOKEN_PROD environment variables"
    exit 1
  fi

  helm upgrade --install \
    --set global.postgresql.auth.password="$DATASOURCE_PASSWORD" \
    --values ./charts/values/postgres-values.yaml \
    --namespace grammr --create-namespace \
    --wait --timeout 60s \
    postgres oci://registry-1.docker.io/bitnamicharts/postgresql

  helm upgrade --install \
    --set image.tag=latest \
    --set spacyModels=ru_core_news_sm \
    --namespace grammr --create-namespace \
    --wait --timeout 60s \
    grammr-morphology ./charts/sidecar-analysis

    helm upgrade --install \
      --set openai_api_key="$OPENAI_API_KEY" \
      --set telegram_token="$TELEGRAM_TOKEN_PROD" \
      --set datasource_password="$DATASOURCE_PASSWORD" \
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