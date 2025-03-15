variable "name" {
  type = string
  description = "The name of the Lambda function"
}

variable "environment_variables" {
  type = map(string)
  description = "Environment variables to set for the Lambda"
  default = {}
}

variable "image_uri" {
  type    = string
  description = "The image to run"
}

variable "memory" {
  type    = number
  default = 128
}

variable "timeout" {
  type    = number
  default = 15
}

variable "root_resource_id" {
  type = string
}

variable "api_gateway_id" {
  type = string
}