terraform {
  source = "git::https://github.com/terraform-aws-modules/terraform-aws-key-pair.git?ref=${local.env_versions.aws_key_pair_module}"
}

locals {
  env_values = yamldecode(file(find_in_parent_folders("env_values.yaml")))
  env_versions = yamldecode(file(find_in_parent_folders("env_versions.yaml")))
}

include "parent" {
  path = find_in_parent_folders("env_configs.hcl")
}

inputs = {
    create = true
    create_private_key = true
    private_key_algorithm = "RSA"
    private_key_rsa_bits = 2048
}
