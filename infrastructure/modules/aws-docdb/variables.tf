variable "docdb_identifier" {
  description = "DocumentDB identifier prefix"
  type        = string
}
variable "environment" {
  description = "Environment name"
  type        = string
}

variable "project" {
  description = "Project name"
  type        = string
}

variable "docdb_username" {
  description = "DocumentDB master username"
  type        = string
}

variable "docdb_password" {
  description = "DocumentDB master password"
  type        = string
  sensitive   = true
}

variable "skip_final_snapshot" {
  default = true
  description = "Skip final snapshot"
}

variable "instance_class" {
    description = "Instance class for DocumentDB instances"
    type        = string
    default     = "db.t3.medium"
}

variable "instance_count" {
  type    = number
  default = 1
}

variable "subnet_ids" {
  type = list(string)
}

variable "vpc_security_group_ids" {
  type = list(string)
}
