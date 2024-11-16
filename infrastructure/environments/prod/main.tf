terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

provider "aws" {
  region  = var.aws_region
  profile = "kjeldsen"
}

module "networking" {
  source = "../../modules/networking"

  project     = var.project_name
  environment = var.environment
  vpc_cidr    = var.vpc_cidr
}

module "database" {
  source = "../../modules/database"

  project            = var.project_name
  environment        = var.environment
  vpc_id             = module.networking.vpc_id
  private_subnet_ids = module.networking.private_subnet_ids
  instance_class     = var.docdb_instance_class
  # These should come from secure sources in production
  master_username = var.docdb_master_username
  master_password = var.docdb_master_password

  # Optional: specify allowed security groups
  allowed_security_groups = [] # Will be populated when we create the backend service
}
