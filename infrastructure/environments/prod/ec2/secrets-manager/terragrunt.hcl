terraform {
  source = "git::https://github.com/terraform-aws-modules/terraform-aws-secrets-manager.git?ref=${local.env_versions.aws_secret_manager_module}"
}

locals {
  env_values = yamldecode(file(find_in_parent_folders("env_values.yaml")))
  env_versions = yamldecode(file(find_in_parent_folders("env_versions.yaml")))
}

include "parent" {
  path = find_in_parent_folders("env_configs.hcl")
}

dependency "key_pair" {
  config_path = "../key-pair"
  mock_outputs = {
    private_key_pem = <<EOF
-----BEGIN RSA PRIVATE KEY-----
MIIEpAIBAAKCAQEAzvVZt3b8m7+Hj1y6tG6v4Yp0kXl5r3FQv+X9y5c5mJv1y5c5m
Jv1y5c5mJv1y5c5mJv1y5c5mJv1y5c5mJv1y5c5mJv1y
-----END RSA PRIVATE KEY-----
EOF
  }
}

inputs = {
  create        = true
  region        = "eu-west-1"
  description   = "Private key for SSH access to EC2 instances in the ${local.env_values.environment} environment"
  name          = "${local.env_values.project}-${local.env_values.environment}-key-pem"
  secret_string = trimspace(dependency.key_pair.outputs.private_key_pem)
}
