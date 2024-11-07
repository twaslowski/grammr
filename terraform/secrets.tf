resource "kubernetes_secret" "telegram_token" {
  metadata {
    name = "telegram-token"
    namespace = local.namespace
  }

  data = {
    telegram_token = var.telegram_token
  }
}

resource "kubernetes_secret" "openai_api_key" {
  metadata {
    name = "openai-api-key"
    namespace = local.namespace
  }

  data = {
    telegram_token = var.openai_api_key
  }
}