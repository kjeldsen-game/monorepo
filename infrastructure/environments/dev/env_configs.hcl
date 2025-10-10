locals {
  region  = "eu-west-1"
  project = "kjeldsen"
  bucket  = "kjeldsen-terraform-state-bucket"
  env_values   = yamldecode(file(find_in_parent_folders("env_values.yaml")))
  env_versions = yamldecode(file(find_in_parent_folders("env_versions.yaml")))
}

remote_state {
  backend = "s3"
  config = {
    bucket         = local.bucket
    key            = "${path_relative_to_include()}/terraform.tfstate"
    region         = local.region
    encrypt        = true
    dynamodb_table = "kjeldsen-terraform-locks"
  }
}

# Provider configuration shared across all modules
generate "provider" {
  path      = "provider.tf"
  if_exists = "overwrite"
  contents  = <<EOF
    terraform {
        required_providers {
            aws = {
            source  = "hashicorp/aws"
            version = "~> 4.0"
            }
        }
    }

    provider "aws" {
        region  = "${local.region}"
        profile = "default"
    }
    
  profile = "default"
}
EOF
}
