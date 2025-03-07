module "morphology" {
  source = "./modules/lambda"

  for_each = {
    for code, lang in local.languages_map :
    code => lang if lang.morphology.enabled == true
  }

  name      = "morphology-${each.key}"
  image_uri = "tobiaswaslowski/grammr-morphology:${var.morphology_image_tag}"
  memory    = 1024
  timeout   = 30
  environment_variables = {
    "SPACY_MODEL" = each.value.model
  }
}
