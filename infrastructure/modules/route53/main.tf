resource "aws_route53_record" "alb" {
  zone_id = var.zone_id
  name    = var.name
  type    = var.type
  alias {
    name                   = var.alias_name
    zone_id                = var.alias_zone_id
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "backend_alb" {
  zone_id = var.zone_id
  name    = var.name
  type    = var.type
  alias {
    name                   = var.alias_name
    zone_id                = var.alias_zone_id
    evaluate_target_health = true
  }
}