locals {
  region  = "eu-west-1"
  project = "kjeldsen"
  bucket  = "kjeldsen-terraform-state-bucket"
  env_values   = yamldecode(file(find_in_parent_folders("env_values.yaml")))
  # env_versions = yamldecode(file(find_in_parent_folders("env_versions.yaml")))
}

remote_state {
  generate = {
    path      = "backend.tf"
    if_exists = "overwrite"
  }
  backend = "s3"
  config = {
    bucket         = local.bucket
    key            = "${path_relative_to_include()}/terraform.tfstate"
    region         = local.region
    encrypt        = true
  }
}

generate "provider" {
  path      = "provider.tf"
  if_exists = "overwrite"
  contents  = <<EOF

    provider "aws" {
        region  = "${local.region}"
        profile = "default"
    }    
EOF
}

generate "versions" {
  path      = "versions.tf"
  if_exists = "overwrite"
  contents  = <<EOF

  terraform {
    required_version = ">= 1.5.7"
    required_providers {
      aws = {
        source  = "hashicorp/aws"
        version = ">= 6.0"
      }
    }
  }  
EOF
}
