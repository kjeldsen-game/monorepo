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

dependency "ec2_sg" {
    config_path = "../../ec2/sg"
    mock_outputs = {
        security_group_id = "sg-123456"
    }
}

inputs = {
    name_prefix         = "${local.env_values.project}-${local.env_values.environment}-docdb-"
    vpc_id              = dependency.vpc.outputs.vpc_id
    ingress_rules = [
        {
            from_port   = 27017
            to_port     = 27017
            protocol    = "tcp"
            security_groups = [dependency.ec2_sg.outputs.security_group_id]
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
        Name        = "${local.env_values.project}-${local.env_values.environment}-docdb"
        Environment = local.env_values.environment
        Project     = local.env_values.project
    }
}
