resource "kubernetes_namespace_v1" "namespace" {
  metadata {
    name = var.namespace
  }
}

resource "helm_release" "postgres" {
  name = "postgres"

  chart     = "oci://registry-1.docker.io/bitnamicharts/postgresql"
  namespace = kubernetes_namespace_v1.namespace.metadata[0].name
  version   = "16.7.14"

  timeout = 180
  wait    = true

  values = [
    file("${path.module}/config/values/${var.environment}/postgres.values.yaml")
  ]

}

resource "helm_release" "grammr_core" {
  name = "grammr-core"

  repository = "https://twaslowski.github.io/grammr-charts"
  chart      = "grammr"
  version    = "0.3.2"

  namespace = kubernetes_namespace_v1.namespace.metadata[0].name
  timeout   = 180
  wait      = true

  values = [
    file("${path.module}/config/values/${var.environment}/core.values.yaml")
  ]

  set = [
    {
      name  = "openai_api_key"
      value = var.openai_api_key
    },
    {
      name  = "image.tag"
      value = data.external.current_commit_hash.result.sha
    }
  ]
}