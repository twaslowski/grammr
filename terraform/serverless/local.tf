locals {
  config                = yamldecode(file("${path.module}/../../environments/${var.environment}/morphology.values.yaml"))
  languages_map = {
    for lang in local.config.languages : lang.code => lang
  }
}