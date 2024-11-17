variable "project" {
  description = "Project name"
  type        = string
}

variable "environment" {
  description = "Environment name"
  type        = string
}

variable "vpc_id" {
  description = "ID of the VPC"
  type        = string
}

variable "subnet_id" {
  description = "ID of the subnet to place the EC2 instance in"
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t2.micro"
}

variable "ecr_repository_url" {
  description = "ECR repository URL"
  type        = string
}

variable "docker_image" {
  description = "Docker image to run"
  type        = string
}

variable "container_external_port" {
  description = "Port the container listens on"
  type        = number
}

variable "container_internal_port" {
  description = "Port the container listens on"
  type        = number
}

variable "environment_vars" {
  description = "Environment variables for the application"
  type        = map(string)
  sensitive   = true
}

variable "ssh_password" {
  description = "Password for the ec2-user"
  type        = string
  sensitive   = true
}

variable "domain_name" {
  description = "Domain name for the backend service"
  type        = string
}

variable "route53_zone_id" {
  description = "Route53 hosted zone ID"
  type        = string
}
