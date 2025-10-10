# ALB ID
output "alb_id" {
  description = "The ID of the ALB"
  value       = aws_lb.main.id
}

# ALB DNS name
output "alb_dns_name" {
  description = "The DNS name of the ALB, used for Route53 alias targets"
  value       = aws_lb.main.dns_name
}

# ALB hosted zone ID (for Route53 alias records)
output "alb_zone_id" {
  description = "The hosted zone ID of the ALB, used for Route53 alias records"
  value       = aws_lb.main.zone_id
}

# Main target group ARN
output "main_target_group_arn" {
  description = "ARN of the main target group"
  value       = aws_lb_target_group.main.arn
}

# Backend target group ARN
output "backend_target_group_arn" {
  description = "ARN of the backend target group"
  value       = aws_lb_target_group.backend.arn
}
