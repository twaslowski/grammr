variable "morphology_image_tag" {
  description = "The tag of the image to deploy"
  type        = string
  default     = "latest"
}

variable "environment" {
  description = "The environment to deploy to"
  type        = string
  default     = "dev"
}