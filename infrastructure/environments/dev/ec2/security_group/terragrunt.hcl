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
dependency "alb" { config_path = find_in_parent_folders("alb") }


inputs = {
  security_group_name_prefix = "${local.env_values.project}-${local.env_values.environment}-sg"
  vpc_id                     = dependency.vpc.outputs.vpc_id

  ingress_rules = [
    {
      from_port   = 22
      to_port     = 22
      protocol    = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
    },
    {
      from_port       = 80
      to_port         = 80
      protocol        = "tcp"
      security_groups = [dependency.alb.outputs.security_group_id]
    },
    {
      from_port       = 8080
      to_port         = 8080
      protocol        = "tcp"
      security_groups = [dependency.alb.outputs.security_group_id]
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
    Name        = "${local.env_values.project}-${local.env_values.environment}-sg"
    Project     = local.env_values.project
    Environment = local.env_values.environment
  }
}
