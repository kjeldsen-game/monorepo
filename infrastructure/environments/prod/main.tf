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

  # Allow the backend security group to connect to DocumentDB
  allowed_security_groups = [module.backend.security_group_id]
}

module "backend" {
  source = "../../modules/backend"

  project     = var.project_name
  environment = var.environment
  vpc_id      = module.networking.vpc_id
  subnet_id   = module.networking.public_subnet_ids[0] # Using first public subnet

  ecr_repository_url      = var.ecr_repository_url
  docker_image            = var.backend_docker_image
  container_external_port = var.container_external_port
  container_internal_port = var.container_internal_port
  ssh_password            = var.ssh_password

  environment_vars = {
    SPRING_PROFILES_ACTIVE = "aws"

    SERVER_PORT          = var.container_internal_port
    ACTUATOR_SERVER_PORT = 8081

    MONGO_HOST     = module.database.cluster_endpoint
    MONGO_PORT     = 27017
    MONGO_USER     = var.docdb_master_username
    MONGO_PASSWORD = var.docdb_master_password
    MONGO_DATABASE = "admin"
    MONGO_OPTIONS  = "?ssl=true&replicaSet=rs0&readPreference=secondaryPreferred&retryWrites=false"

    ACCESS_TOKEN_VALIDITY_SECONDS = 86400

    PUBLIC_KEY  = var.public_key
    PRIVATE_KEY = var.private_key
  }
}
