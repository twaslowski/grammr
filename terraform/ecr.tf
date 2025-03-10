module "morphology_repository" {
  source = "terraform-aws-modules/ecr/aws"

  repository_name                 = "morphology"
  repository_image_tag_mutability = "MUTABLE"

  repository_read_access_arns = [for lambda_instance in module.morphology_lambda : lambda_instance.lambda_role_arn]

  repository_lifecycle_policy = local.repository_lifecycle_policy
}

locals {
  repository_lifecycle_policy = jsonencode({
    rules = [
      {
        rulePriority = 1,
        description  = "Keep 3 images",
        selection = {
          tagStatus   = "any",
          countType   = "imageCountMoreThan",
          countNumber = 14
        },
        action = {
          type = "expire"
        }
      }
    ]
  })
}