terraform {
  source = "../../../../modules/aws-iam"
}

locals {
    assume_role_policy = file("${get_terragrunt_dir()}/assume_role_policy.json")
    iam_role_policy    = file("${get_terragrunt_dir()}/iam_role_policy.json")
    env_values   = yamldecode(file(find_in_parent_folders("env_values.yaml")))
}

include "parent" {
    path = find_in_parent_folders("env_configs.hcl")
}

inputs = {
    iam_role_name             = "${local.env_values.project}-${local.env_values.environment}"
    iam_assume_role_policy    = local.assume_role_policy

    iam_role_policy_name      = "${local.env_values.project}-${local.env_values.environment}-ecr-policy"
    iam_role_policy_document  = local.iam_role_policy

    iam_instance_profile_name = "${local.env_values.project}-${local.env_values.environment}"
}
