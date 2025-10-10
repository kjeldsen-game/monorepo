variable "environment" {
  description = "Environment name"
  type        = string
}

variable "project" {
  description = "Project name"
  type        = string
}
# variables.tf

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.small"
}

variable "subnet_id" {
  description = "Subnet ID where EC2 will be launched"
  type        = string
}

variable "vpc_security_group_ids" {
  description = "List of security group IDs for EC2"
  type        = list(string)
}

variable "iam_instance_profile" {
  description = "IAM instance profile for EC2"
  type        = string
}

variable "associate_public_ip_address" {
  description = "Whether to assign a public IP to the EC2 instance"
  type        = bool
  default     = true
}

variable "aws_key_pair" {
  description = "Name of the AWS key pair for SSH access"
  type        = string
}

variable "ssh_password" {
  description = "SSH password for the instance (if used in user_data)"
  type        = string
  sensitive   = true
}

variable "docdb_master_username" {
  description = "DocDB master username"
  type        = string
}

variable "docdb_master_password" {
  description = "DocDB master password"
  type        = string
  sensitive   = true
}

variable "public_key" {
  description = "Public key for the application"
  type        = string
}

variable "private_key" {
  description = "Private key for the application"
  type        = string
  sensitive   = true
}

variable "environment_vars" {
  description = "Map of environment variables for user_data script"
  type        = map(any)
  default     = {
    SPRING_PROFILES_ACTIVE        = "aws"
    SERVER_PORT                   = 8080
    ACTUATOR_SERVER_PORT          = 8081
    MONGO_PORT                    = 27017
    MONGO_DATABASE                = "admin"
    MONGO_OPTIONS                 = "?replicaSet=rs0&readPreference=secondaryPreferred&retryWrites=false"
    ACCESS_TOKEN_VALIDITY_SECONDS = 86400
    NEXTAUTH_SECRET               = ""
    NEXT_AUTH_BACKEND_URL         = ""
    NEXT_PUBLIC_BACKEND_URL       = ""
    NEXTAUTH_URL                  = ""
  }
}
