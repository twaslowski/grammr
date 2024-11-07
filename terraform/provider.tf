terraform {
  backend "kubernetes" {
    secret_suffix = "open-mood-tracker-state"
    config_path = "~/.kube/config"
  }
}

provider "helm" {
  kubernetes {
    config_path = "~/.kube/config"
  }
}

provider "kubernetes" {
}
