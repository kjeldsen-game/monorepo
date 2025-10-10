terraform {
  source = "../../../modules/docdb"
}

include "parent" {
  path = find_in_parent_folders("env_configs.hcl")
}

locals {
    env_values   = yamldecode(file(find_in_parent_folders("env_values.yaml")))
    env_versions = yamldecode(file(find_in_parent_folders("env_versions.yaml")))
    kjeldsen_secrets = jsondecode(
        run_cmd("aws","secretsmanager","get-secret-value","--secret-id", "prod/kjeldsen-app-secret","--query", "SecretString","--output", "text")
    )
}

dependency "vpc" { config_path = find_in_parent_folders("vpc") }
dependency "ec2" { config_path = find_in_parent_folders("ec2") }


inputs = {
  project     = local.env_values.project
  environment = local.env_values.environment

  docdb_master_username = local.kjeldsen_secrets.docdb_username
  docdb_master_password = local.kjeldsen_secrets.docdb_password

  skip_final_snapshot = true
  instance_class      = "db.t3.medium"
  instance_count      = 1

  subnet_ids             = dependency.vpc.outputs.private_subnet_ids
  vpc_id                 = dependency.vpc.outputs.vpc_id
  vpc_security_group_ids = [dependency.ec2.outputs.security_group_id]
  allowed_security_groups = [dependency.ec2.outputs.security_group_id]
}
