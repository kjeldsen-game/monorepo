resource "aws_iam_role" "default" {
  name               = var.iam_role_name
  assume_role_policy = var.iam_assume_role_policy
}

resource "aws_iam_instance_profile" "default" {
  name = var.iam_instance_profile_name
  role = aws_iam_role.default.name
}

resource "aws_iam_role_policy" "default" {
  name   = var.iam_role_policy_name
  role   = aws_iam_role.default.id
  policy = var.iam_role_policy_document
}
