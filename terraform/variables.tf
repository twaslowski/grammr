variable "morphology_image_version" {
  description = "The tag of the image to deploy"
  type        = string
}

variable "environment" {
  description = "The environment to deploy to"
  type        = string
  default     = "dev"
}