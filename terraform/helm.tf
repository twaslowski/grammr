resource "helm_release" "application" {
  chart            = "../charts/grammr"
  name             = "grammr"
  namespace        = local.namespace
  create_namespace = true

  set {
    name  = "environmentVariables[0].name"
    value = "SPRING_PROFILES_ACTIVE"
  }

  set {
    name  = "environmentVariables[0].value"
    value = "prod"
  }

  set {
    name  = "environmentVariables[1].name"
    value = "TELEGRAM_TOKEN"
  }

  set_sensitive {
    name  = "environmentVariables[1].value"
    value = var.telegram_token
  }

  set {
    name  = "environmentVariables[2].name"
    value = "OPENAI_API_KEY"
  }

  set_sensitive {
    name  = "environmentVariables[2].value"
    value = var.openai_api_key
  }

  timeout = 150
}