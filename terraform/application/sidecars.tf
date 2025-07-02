resource "helm_release" "morphology_ru" {
  name = "inflection-ru"

  repository = "https://twaslowski.github.io/grammr-charts"
  chart      = "inflection-ru"
  version    = "0.2.3"

  namespace = kubernetes_namespace_v1.namespace.metadata[0].name
  timeout   = 180
  wait      = true
}

resource "helm_release" "multi_inflection" {
  name       = "multi-inflection"
  chart      = "multi-inflection"
  repository = "https://twaslowski.github.io/grammr-charts"
  version    = "0.2.2"

  values = [
    file("${path.module}/config/values/${var.environment}/multi-inflection.values.yaml")
  ]

  namespace = kubernetes_namespace_v1.namespace.metadata[0].name
  timeout   = 450
  wait      = true
}

resource "helm_release" "anki_exporter" {
  name       = "anki-exporter"
  chart      = "anki-exporter"
  repository = "https://twaslowski.github.io/grammr-charts"
  version    = "0.2.0"

  namespace = kubernetes_namespace_v1.namespace.metadata[0].name
  timeout   = 180
  wait      = true
}