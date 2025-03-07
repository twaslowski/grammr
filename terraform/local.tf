locals {
  config = yamldecode(file("${path.module}/../charts/values/grammr.values.yaml"))

  languages_map = {
    for lang in local.config.languages : lang.code => lang
  }
}