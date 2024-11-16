# CIDR block for the VPC - defines the IP range for the entire network
variable "vpc_cidr" {
  description = "CIDR block for VPC"
  type        = string
}

# Environment name - used for resource naming and tagging (e.g., prod, dev, staging)
variable "environment" {
  description = "Environment name (e.g. prod, dev, staging)"
  type        = string
}

# Project name - used for resource naming and tagging
variable "project" {
  description = "Project name"
  type        = string
}
