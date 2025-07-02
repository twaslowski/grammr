resource "helm_release" "core_db_util" {
  name = "core-db-util"

  repository = "https://twaslowski.github.io/grammr-charts"
  chart      = "core-db-util"
  version    = "0.3.1"

  namespace = kubernetes_namespace_v1.namespace.metadata[0].name
  timeout   = 30
  wait      = true

  set = [
    {
      name  = "database_connection_string"
      value = "postgres.${var.namespace}.svc.cluster.local"
    }
  ]
}