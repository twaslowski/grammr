module "morphology_repository" {
  source = "terraform-aws-modules/ecr/aws"

  repository_name                 = "morphology"
  repository_image_tag_mutability = "MUTABLE"

  create_repository_policy = true
  registry_policy = local.registry_policy

  repository_lifecycle_policy = local.repository_lifecycle_policy
}

module "multi_inflection_repository" {
  source = "terraform-aws-modules/ecr/aws"

  repository_name                 = "multi-inflection"
  repository_image_tag_mutability = "MUTABLE"

  create_repository_policy = true
  registry_policy = local.registry_policy

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

  registry_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "AllowAccountPullAccess"
        Effect = "Allow"
        Principal = {
          AWS = "arn:aws:iam::246770851643:root"
        }
        Action = [
          "ecr:BatchCheckLayerAvailability",
          "ecr:BatchGetImage",
          "ecr:DescribeImageScanFindings",
          "ecr:DescribeImages",
          "ecr:DescribeRepositories",
          "ecr:GetAuthorizationToken",
          "ecr:GetDownloadUrlForLayer",
          "ecr:GetLifecyclePolicy",
          "ecr:GetLifecyclePolicyPreview",
          "ecr:GetRepositoryPolicy",
          "ecr:ListImages",
          "ecr:ListTagsForResource"
        ]
      }
    ]
  })
}