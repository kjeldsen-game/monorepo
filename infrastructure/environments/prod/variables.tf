variable "project" {
  description = "Name of the project"
  type        = string
  default     = "kjeldsen"
}

variable "s3_bucket" {
  description = "The name of the S3 bucket"
  type        = string
  default     = "db-persistence"
}

variable "environment" {
  description = "Environment name"
  type        = string
  default     = "prod"
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
