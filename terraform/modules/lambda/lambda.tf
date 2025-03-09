module "lambda" {
  source        = "terraform-aws-modules/lambda/aws"
  function_name = var.name
  description   = "Provides ${var.name} container"

  package_type                            = "Image"
  architectures                           = ["arm64"]
  create_package                          = false

  image_uri              = var.image_uri
  memory_size            = var.memory

  cloudwatch_logs_retention_in_days = 14

  # https://github.com/terraform-aws-modules/terraform-aws-lambda/issues/36#issuecomment-650217274
  create_current_version_allowed_triggers = false

  timeout          = var.timeout
  allowed_triggers = local.allowed_triggers

  environment_variables = var.environment_variables
}

resource "aws_cloudwatch_event_rule" "keep_warm" {
  name                = "${var.name}-keep-warm"
  description         = "Fires every ten minutes"
  schedule_expression = "rate(10 minutes)"
}

resource "aws_cloudwatch_event_target" "keep_warm" {
  rule      = aws_cloudwatch_event_rule.keep_warm.name
  target_id = "${module.lambda.lambda_function_name}-keep-warm"
  arn       = module.lambda.lambda_function_arn

  input_transformer {
    input_template = <<JSON
    {
      "body": "{\"keep-warm\": \"true\"}"
    }
  JSON
  }
}

locals {
  allowed_triggers = {
    apigateway = {
      service    = "apigateway"
      source_arn = "arn:aws:execute-api:eu-central-1:${data.aws_caller_identity.current.account_id}:*"
    },
    events = {
      service    = "events"
      source_arn = "arn:aws:events:eu-central-1:${data.aws_caller_identity.current.account_id}:rule/*"
    }
  }
}
