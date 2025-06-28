module "polly_lambda" {
  source  = "terraform-aws-modules/lambda/aws"

  function_name = "grammr-tts-${var.environment}"
  description   = "Lambda function for AWS Polly TTS"
  handler       = "polly.lambda_handler"
  runtime       = "python3.12"
  memory_size   = 256
  timeout       = 30
  source_path   = "${path.module}/lambda/tts"

  cloudwatch_logs_retention_in_days = 14

  # https://github.com/terraform-aws-modules/terraform-aws-lambda/issues/36#issuecomment-650217274
  create_current_version_allowed_triggers = false
  allowed_triggers = local.allowed_triggers

  attach_policy_json = true
  policy_json = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "polly:SynthesizeSpeech",
        ]
        Resource = "*"
      }
    ]
  })
}

locals {
  allowed_triggers = {
    apigateway = {
      service    = "apigateway"
      source_arn = "arn:aws:execute-api:eu-central-1:${data.aws_caller_identity.current.account_id}:*"
    },
  }
}

resource "aws_api_gateway_resource" "resource" {
  rest_api_id = aws_api_gateway_rest_api.grammr_api.id
  parent_id   = aws_api_gateway_rest_api.grammr_api.root_resource_id
  path_part   = "tts"
}

resource "aws_api_gateway_method" "get" {
  rest_api_id   = aws_api_gateway_rest_api.grammr_api.id
  resource_id   = aws_api_gateway_resource.resource.id
  http_method   = "POST"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "lambda_integration" {
  rest_api_id             = aws_api_gateway_rest_api.grammr_api.id
  resource_id             = aws_api_gateway_resource.resource.id
  http_method             = aws_api_gateway_method.get.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = module.polly_lambda.lambda_function_invoke_arn
}


