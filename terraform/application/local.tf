locals {
  config                = yamldecode(file("${path.module}/../../environments/${var.environment}/morphology.values.yaml"))
  multi_inflection_config = yamldecode(file("${path.module}/../../environments/${var.environment}/multi_inflection.values.yaml"))

  languages_map = {
    for lang in local.config.languages : lang.code => lang
  }

  multi_inflection_languages = multi_inflections_config.languageCodes
}