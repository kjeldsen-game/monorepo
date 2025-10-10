variable "project" {
  description = "The name of the project"
  type        = string
}

variable "vpc_name" {
  description = "The name of the project"
  type        = string
}


variable "environment" {
  description = "The environment name (e.g., dev, prod)"
  type        = string
}

variable "vpc_cidr" {
  description = "The CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "public_subnet_count" {
  description = "Number of public subnets to create"
  type        = number
  default     = 2
}
