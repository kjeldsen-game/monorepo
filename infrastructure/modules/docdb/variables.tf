variable "environment" {
  description = "Environment name"
  type        = string
}

variable "project" {
  description = "Project name"
  type        = string
}

variable "docdb_master_username" {
  description = "DocumentDB master username"
  type        = string
}

variable "docdb_master_password" {
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

variable "vpc_id" {
  type = string
}
variable "allowed_security_groups" {
  description = "List of security groups that can connect to DocDB"
  type        = list(string)
  default     = []
}