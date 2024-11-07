resource "helm_release" "application" {
  chart = "../charts/open-mood-tracker"
  name = "open-mood-tracker"
  namespace = "open-mood-tracker"

  values = [
    <<YAML
    environmentVariables:
      - name: SPRING_PROFILES_ACTIVE
        value: prod
      - name: TELEGRAM_TOKEN
        valueFrom:
            secretKeyRef:
                name: telegram-token
                key: telegram_token
      - name: OPENAI_API_KEY
        valueFrom:
            secretKeyRef:
                name: openai-api-key
                key: openai_api_key
    YAML
  ]

  timeout = 150
}