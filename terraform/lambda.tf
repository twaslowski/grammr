module "morphology" {
  source = "./modules/lambda"

  for_each = {
    for code, lang in local.languages_map :
    code => lang if lang.morphology.enabled == true
  }

  name      = "morphology-${each.key}"
  image_uri = "246770851643.dkr.ecr.eu-central-1.amazonaws.com/morphology:lambda-0.4.1"
  memory    = 1024
  timeout   = 30

  api_gateway_id   = aws_api_gateway_rest_api.grammr_api.id
  root_resource_id = aws_api_gateway_rest_api.grammr_api.root_resource_id

  environment_variables = {
    "SPACY_MODEL" = each.value.model
  }
}
