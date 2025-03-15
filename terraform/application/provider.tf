terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  backend "s3" {}
}

provider "aws" {
  default_tags {
    tags = {
      "created-by" = "terraform",
      "environment" = var.environment,
      "application" = "grammr"
    }
  }
}