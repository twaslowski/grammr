resource "aws_api_gateway_rest_api" "grammr_api" {
  name        = "grammr-api-${var.environment}"
  description = "The serverless grammr API"

  binary_media_types = ["audio/mpeg"]
}

resource "aws_api_gateway_deployment" "deployment" {
  rest_api_id = aws_api_gateway_rest_api.grammr_api.id

  triggers = {
    redeployment = join("", [
      filesha1("api_gateway.tf"),
      filesha1("tts.tf"),
    ])
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_api_gateway_stage" "stage" {
  deployment_id = aws_api_gateway_deployment.deployment.id
  rest_api_id   = aws_api_gateway_rest_api.grammr_api.id
  stage_name    = var.environment
}

resource "aws_api_gateway_method_settings" "example" {
  rest_api_id = aws_api_gateway_rest_api.grammr_api.id
  stage_name  = aws_api_gateway_stage.stage.stage_name
  method_path = "*/*"

  settings {
    metrics_enabled = true
    logging_level   = "INFO"
  }
}

resource "aws_cloudwatch_log_group" "logs" {
  name              = "API-Gateway-Execution-Logs_${aws_api_gateway_rest_api.grammr_api.id}/${var.environment}"
  retention_in_days = 14
}
