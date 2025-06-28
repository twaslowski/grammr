resource "kubernetes_namespace_v1" "namespace" {
  metadata {
    name = "grammr-${var.environment}"
  }
}

resource "helm_release" "postgres" {
  name      = "postgres"
  chart     = "oci://registry-1.docker.io/bitnamicharts/postgresql"
  namespace = kubernetes_namespace_v1.namespace.metadata[0].name
  version   = "16.7.14"

  values = [
    file("${path.module}/config/values/${var.environment}/postgres.values.yaml")
  ]

  timeout = 180
  wait    = true
}

resource "helm_release" "grammr_core" {
  name       = "grammr-core"
  chart      = "grammr-core"
  repository = "https://twaslowski.github.io/grammr-charts"
  version    = "0.3.2"

  namespace = kubernetes_namespace_v1.namespace.metadata[0].name

  values = [
    file("${path.module}/config/values/${var.environment}/core.values.yaml")
  ]

  set {
    name  = "openai_api_key"
    value = var.openai_api_key
  }

  set {
    name  = "image.tag"
    value = data.external.git_hash.result
  }

  timeout = 180
  wait    = true
}