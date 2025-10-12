terraform {
  source = "../../../modules/aws-docdb"
}

locals {
    env_values   = yamldecode(file(find_in_parent_folders("env_values.yaml")))
    kjeldsen_secrets = jsondecode(
        run_cmd("aws","secretsmanager","get-secret-value","--secret-id", "prod/kjeldsen-app-secret","--query", "SecretString","--output", "text")
    )
}

include "parent" {
  path = find_in_parent_folders("env_configs.hcl")
}

dependency "docdb_sg" { 
  config_path = "./sg" 
  mock_outputs = {
    security_group_id = "mock-security-group"
  }  
}

dependency "vpc" {
  config_path = "../vpc"
  mock_outputs = {
    private_subnet_ids = ["subnet-12345678", "subnet-87654321"]
  }
}

inputs = {
    docdb_identifier      = "${local.env_values.project}-${local.env_values.environment}-docdb"

    docdb_username        = local.kjeldsen_secrets.docdb_username
    docdb_password        = local.kjeldsen_secrets.docdb_password

    environment           = local.env_values.environment
    project               = local.env_values.project

    skip_final_snapshot   = true
    instance_class        = "db.t3.medium"
    instance_count        = 1

    vpc_security_group_ids = [dependency.docdb_sg.outputs.security_group_id]
    subnet_ids            =   dependency.vpc.outputs.private_subnet_ids
}
