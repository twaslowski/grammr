variable "grafana_api_key" {
  description = "Service account token for Grafana API access"
  type        = string
  sensitive   = true
}

variable "grafana_url" {
  description = "URL of the Grafana instance"
  type        = string
  default     = "https://grafana.twaslowski.com"
}