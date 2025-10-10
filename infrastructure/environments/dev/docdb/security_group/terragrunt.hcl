terraform {
  source = "../../../../modules/security_group"
}

locals {
    env_values   = yamldecode(file(find_in_parent_folders("env_values.yaml")))
    env_versions = yamldecode(file(find_in_parent_folders("env_versions.yaml")))
}

include "parent" {
    path = find_in_parent_folders("env_configs.hcl")
}

dependency "vpc" { config_path = find_in_parent_folders("vpc") }
dependency "ec2" { config_path = find_in_parent_folders("ec2") }

inputs = {
  security_group_name_prefix = "${local.env_values.project}-${local.env_values.environment}-docdb-"
  vpc_id                     = dependency.vpc.outputs.vpc_id

  ingress_rules = [
    {
      from_port       = 27017
      to_port         = 27017
      protocol        = "tcp"
      security_groups = dependency.ec2.outputs.security_group_id
    }
  ]

  egress_rules = [
    {
      from_port   = 0
      to_port     = 0
      protocol    = "-1"
      cidr_blocks = ["0.0.0.0/0"]
    }
  ]

  tags = {
    Name        = "${local.env_values.project}-${local.env_values.environment}-docdb"
    Project     = local.env_values.project
    Environment = local.env_values.environment
  }
}

