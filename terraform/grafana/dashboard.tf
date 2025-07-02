resource "grafana_folder" "base_folder" {
  title = "grammr"
}

resource "grafana_dashboard" "lambdas" {
  config_json = file("${path.module}/dashboards/lambdas.json")
  folder = grafana_folder.base_folder.id
}

resource "grafana_dashboard" "core" {
  config_json = file("${path.module}/dashboards/core.json")
  folder = grafana_folder.base_folder.id
}