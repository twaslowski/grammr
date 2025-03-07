module "lambda" {
  source        = "terraform-aws-modules/lambda/aws"
  function_name = var.name
  description   = "Provides ${var.name} container"

  package_type                            = "Image"
  architectures                           = ["x86_64"]
  create_package                          = false

  image_uri              = var.image_uri
  memory_size            = var.memory

  cloudwatch_logs_retention_in_days = 14

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
      "body": "{\"pre_warm\": \"true\"}"
    }
  JSON
  }
}

locals {
  allowed_triggers = {
    events = {
      service    = "events"
      source_arn = "arn:aws:events:eu-central-1:${data.aws_caller_identity.current.account_id}:rule/*"
    }
  }
}
