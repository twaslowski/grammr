locals {
  config = yamldecode(file("${path.module}/../../environments/${var.environment}/core.values.yaml"))
  create_ecr_repository = var.environment == "dev"

  languages_map = {
    for lang in local.config.languages : lang.code => lang
  }
}