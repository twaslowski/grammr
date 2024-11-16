terraform {
  backend "kubernetes" {
    secret_suffix = "grammr-state"
  }
}

provider "helm" {
}

provider "kubernetes" {
}
