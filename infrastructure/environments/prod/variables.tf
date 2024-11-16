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
