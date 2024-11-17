output "instance_public_ip" {
  description = "Public IP of the EC2 instance"
  value       = aws_instance.backend.public_ip
}

output "instance_id" {
  description = "ID of the EC2 instance"
  value       = aws_instance.backend.id
}

output "ssh_connection_string" {
  description = "SSH connection string for the EC2 instance"
  value       = "ssh ec2-user@${aws_instance.backend.public_ip}"
}

output "security_group_id" {
  description = "ID of the backend security group"
  value       = aws_security_group.backend.id
}

output "domain_name" {
  description = "Domain name of the backend service"
  value       = var.domain_name
}

output "public_ip" {
  description = "Public IP of the EC2 instance"
  value       = aws_eip.backend.public_ip
}
