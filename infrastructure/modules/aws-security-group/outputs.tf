output "security_group_id" {
  description = "The ID of the security group"
  value       = aws_security_group.default.id
}

output "security_group_name" {
  description = "The name of the security group"
  value       = aws_security_group.default.name
}
