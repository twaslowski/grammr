variable "environment" {
  description = "The deployment environment (dev or prod)"
  type        = string
}

variable "openai_api_key" {
  description = "OpenAI API key for grammr-core"
  type        = string
  sensitive   = true
}

variable "namespace" {
  description = "Kubernetes namespace for the application"
  type        = string
}
