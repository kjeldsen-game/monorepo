terraform {
  source = "git::https://github.com/terraform-aws-modules/terraform-aws-acm.git?ref=v4.0.0"
}

include "parent" {
  path = find_in_parent_folders("env_configs.hcl")
}

locals {
    env_values   = yamldecode(file(find_in_parent_folders("env_values.yaml")))
    env_versions = yamldecode(file(find_in_parent_folders("env_versions.yaml")))
}

inputs = {
    domain_name = "kjeldsengame.com"
    subject_alternative_names = [
        "backend.kjeldsengame.com"
    ]
    validation_method      = "DNS"
    create_route53_records = false
    tags = {
        Name        = "${local.env_values.project}-${local.env_values.environment}-acm"
        Environment = local.env_values.environment
        Project     = local.env_values.project
    }
}