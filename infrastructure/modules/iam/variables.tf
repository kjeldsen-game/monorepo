variable "iam_role_name" {
  description = "Name of the IAM role"
  type        = string
}

variable "iam_instance_profile_name" {
  description = "Name of the IAM instance profile"
  type        = string
}

variable "iam_role_policy_name" {
  description = "Name of the IAM role policy"
  type        = string
}

variable "iam_assume_role_policy" {
  description = "Assume role policy JSON for the IAM role"
  type        = string
}

variable "iam_role_policy_document" {
  description = "Policy JSON for the IAM role"
  type        = string
}
