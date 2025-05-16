variable "vercel_token" {
  description = "Vercel token for authentication"
  type        = string
  sensitive   = true
}

variable "backend_host_preview" {}
variable "backend_host_production" {}
variable "clerk_publishable_key_preview" {}
variable "clerk_publishable_key_production" {}

variable "clerk_secret_key_preview" {
  sensitive = true
}

variable "clerk_secret_key_production" {
  sensitive = true
}
