locals {
  config = yamldecode(file("${path.module}/../config.yaml"))

  languages_map = {
    for lang in local.config.languages : lang.code => lang
  }
}