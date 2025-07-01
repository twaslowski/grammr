resource "grafana_folder" "base_folder" {
  title = "grammr"
}

resource "grafana_dashboard" "lambdas" {
  config_json = file("${path.module}/dashboards/lambdas.json")
  folder = grafana_folder.base_folder.id
}