variable "zone_id" {
  description = "The Route 53 hosted zone ID"
  type        = string
}

variable "name" {
  description = "The name of the DNS record"
  type        = string
}

variable "type" {
  description = "The type of DNS record (e.g., A, CNAME)"
  type        = string
  default     = "A"
}

variable "alias_name" {
  description = "The DNS name of the AWS resource to which you are routing traffic (e.g., ALB DNS name)"
  type        = string
}

variable "alias_zone_id" {
  description = "The hosted zone ID of the AWS resource specified in alias_name"
  type        = string
}
