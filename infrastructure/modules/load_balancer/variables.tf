# Project & environment
variable "project" {
  description = "The project name"
  type        = string
}

variable "environment" {
  description = "The environment name (dev, staging, prod, etc.)"
  type        = string
}

# ALB configuration
variable "alb_name" {
  description = "Name prefix for the ALB and target groups"
  type        = string
}

variable "alb_type" {
  description = "ALB type (application or network)"
  type        = string
  default     = "application"
}

variable "alb_security_group_id" {
  description = "Security group ID to attach to the ALB"
  type        = string
}

variable "public_subnets" {
  description = "List of public subnet IDs to deploy the ALB in"
  type        = list(string)
}

# VPC configuration
variable "vpc_id" {
  description = "VPC ID where the ALB and target groups will be deployed"
  type        = string
}

# Target group configuration
variable "main_target_port" {
  description = "Port for the main target group"
  type        = number
  default     = 80
}

variable "main_health_path" {
  description = "Health check path for the main target group"
  type        = string
  default     = "/"
}

variable "backend_target_port" {
  description = "Port for the backend target group"
  type        = number
  default     = 8080
}

variable "backend_health_path" {
  description = "Health check path for the backend target group"
  type        = string
  default     = "/swagger-ui/index.html"
}

# EC2 instances
variable "ec2_instance_id" {
  description = "The EC2 instance ID to attach to target groups"
  type        = string
}

# ACM / HTTPS
variable "acm_certificate_arn" {
  description = "ACM certificate ARN for HTTPS listener"
  type        = string
}

variable "backend_host" {
  description = "Backend host for listener rule (e.g., backend.example.com)"
  type        = string
  default     = "backend.example.com"
}

# Tags
variable "tags" {
  description = "Tags to apply to all resources"
  type        = map(string)
  default     = {}
}
