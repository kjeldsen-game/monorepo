terraform {
  source = "../../../modules/aws-vpc"
}

locals {
  env_values   = yamldecode(file(find_in_parent_folders("env_values.yaml")))
}

include "parent" {
  path = find_in_parent_folders("env_configs.hcl")
}

inputs = {
  vpc_name             = "${local.env_values.project}-${local.env_values.environment}"
  project              = local.env_values.project
  environment          = local.env_values.environment
  vpc_cidr             = "10.0.0.0/16"
  public_subnet_count  = 2
}
