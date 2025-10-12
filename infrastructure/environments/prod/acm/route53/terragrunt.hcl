terraform {
  source = "git::https://github.com/terraform-aws-modules/terraform-aws-acm.git?ref=${local.env_versions.aws_acm_module}"
}

locals {
  env_values = yamldecode(file(find_in_parent_folders("env_values.yaml")))
  env_versions = yamldecode(file(find_in_parent_folders("env_versions.yaml")))
}

include "parent" {
  path = find_in_parent_folders("env_configs.hcl")
}

dependency "acm" {
  config_path = "../terragrunt.hcl"
  mock_outputs = {
    distinct_domain_names = ["kjeldsengame.com", "backend.kjeldsengame.com"]
    acm_certificate_domain_validation_options = [
      {
        domain_name       = "kjeldsengame.com"
        resource_record_name  = "_abc123.kjeldsengame.com"
        resource_record_type  = "CNAME"
        resource_record_value = "xyz.acm-validations.aws"
      },
      {
        domain_name       = "backend.kjeldsengame.com"
        resource_record_name  = "_def456.backend.kjeldsengame.com"
        resource_record_type  = "CNAME"
        resource_record_value = "uvw.acm-validations.aws"
      }
    ]
  }
}

inputs = {
  create_certificate                  = false
  create_route53_records_only         = true
  validation_method                   = "DNS"
  distinct_domain_names               = dependency.acm.outputs.distinct_domain_names
  acm_certificate_domain_validation_options = dependency.acm.outputs.acm_certificate_domain_validation_options
  zone_id                             = "Z0530348E6EUGD6L57W7"
}
