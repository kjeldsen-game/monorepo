terraform {
  source = "github.com/terraform-aws-modules/terraform-aws-ec2-instance.git//?ref=${local.env_versions.aws_ec2_instance_version}"
}

locals {
    env_values   = yamldecode(file(find_in_parent_folders("env_values.yaml")))
    env_versions = yamldecode(file(find_in_parent_folders("env_versions.yaml")))
}

include "parent" {
  path = find_in_parent_folders("env_configs.hcl")
}

dependency "iam" { config_path = find_in_parent_folders("ec2/iam") }

inputs = {
    
}
