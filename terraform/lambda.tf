module "morphology_lambda" {
  source = "./modules/lambda"

  for_each = {
    for code, lang in local.languages_map :
    code => lang if lang.morphology.enabled == true
  }

  name      = "morphology-${each.key}"
  image_uri = "246770851643.dkr.ecr.eu-central-1.amazonaws.com/morphology:${var.morphology_image_version}-${each.key}"
  memory    = 2048
  timeout   = 90

  api_gateway_id   = aws_api_gateway_rest_api.grammr_api.id
  root_resource_id = aws_api_gateway_rest_api.grammr_api.root_resource_id
}
