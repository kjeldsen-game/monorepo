# This file defines what variables are needed and their types
variable "aws_region" {
  description = "AWS region to deploy resources"
  type        = string
}

variable "project_name" {
  description = "Name of the project"
  type        = string
}

variable "environment" {
  description = "Environment name"
  type        = string
}

variable "vpc_cidr" {
  description = "CIDR block for VPC"
  type        = string
}

variable "docdb_instance_class" {
  description = "Instance class for DocumentDB"
  type        = string
}

variable "docdb_master_username" {
  description = "Master username for DocumentDB"
  type        = string
  sensitive   = true
}

variable "docdb_master_password" {
  description = "Master password for DocumentDB"
  type        = string
  sensitive   = true
}

variable "ecr_repository_url" {
  description = "ECR repository URL"
  type        = string
}

variable "backend_docker_image" {
  description = "Docker image for the backend service"
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

variable "ssh_password" {
  description = "Password for the ec2-user"
  type        = string
  sensitive   = true
}

variable "public_key" {
  description = "Public key for the backend service"
  type        = string
}

variable "private_key" {
  description = "Private key for the backend service"
  type        = string
  sensitive   = true
}
