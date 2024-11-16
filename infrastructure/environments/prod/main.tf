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
