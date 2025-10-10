terraform {
  source = "git::https://github.com/terraform-aws-modules/terraform-aws-acm.git?ref=v4.0.0"
}

locals {
  env_values = yamldecode(file(find_in_parent_folders("env_values.yaml")))
}

dependency "acm" {
  config_path = "../acm"
}

inputs = {
  create_certificate                  = false
  create_route53_records_only         = true
  validation_method                   = "DNS"
  distinct_domain_names               = dependency.acm.outputs.distinct_domain_names
  acm_certificate_domain_validation_options = dependency.acm.outputs.acm_certificate_domain_validation_options
  zone_id                             = "Z0530348E6EUGD6L57W7"
}
