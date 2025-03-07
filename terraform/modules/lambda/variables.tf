variable "name" {
  type = string
  description = "The name of the Lambda function"
}

variable "environment_variables" {
  type = map(string)
  description = "Environment variables to set for the Lambda"
}

variable "image_uri" {
  type    = string
  description = "The image to run"
  default = null
}

variable "memory" {
  type    = number
  default = 128
}

variable "timeout" {
  type    = number
  default = 15
}
