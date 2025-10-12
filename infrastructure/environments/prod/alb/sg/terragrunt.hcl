terraform {
  source = "../../../../modules/aws-security-group"
}

locals {
  env_values   = yamldecode(file(find_in_parent_folders("env_values.yaml")))
}

include "parent" {
  path = find_in_parent_folders("env_configs.hcl")
}

dependency "vpc" {
    config_path = "../../vpc"
    mock_outputs = {
        vpc_id = "vpc-123456"
    }
}

inputs = {
    name_prefix         = "${local.env_values.project}-${local.env_values.environment}-alb-"
    vpc_id              = dependency.vpc.outputs.vpc_id
    ingress_rules = [
        {
            from_port   = 80
            to_port     = 80
            protocol    = "tcp"
            cidr_blocks = ["0.0.0.0/0"]
        }, 
        {
            from_port   = 443
            to_port     = 443
            protocol    = "tcp"
            cidr_blocks = ["0.0.0.0/0"]
        }, 
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
        Name        = "${local.env_values.project}-${local.env_values.environment}-alb-sg"
        Environment = local.env_values.environment
        Project     = local.env_values.project
    }
}
