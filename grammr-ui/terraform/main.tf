terraform {
  backend "s3" {}
  required_providers {
    vercel = {
      source  = "vercel/vercel"
      version = "~> 3.0"
    }
  }
}

provider "vercel" {
  api_token = var.vercel_token
}
