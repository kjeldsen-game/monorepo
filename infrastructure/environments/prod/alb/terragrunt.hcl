terraform {
    source = "git::https://github.com/terraform-aws-modules/terraform-aws-alb.git?ref=${local.env_versions.aws_alb_module}"
}

locals {
    env_values = yamldecode(file(find_in_parent_folders("env_values.yaml")))
    env_versions = yamldecode(file(find_in_parent_folders("env_versions.yaml")))
}

include "parent" {
    path = find_in_parent_folders("env_configs.hcl")
}

dependency "vpc" {
    config_path = "../vpc"
}


dependency "alb_sg" {
    config_path = "./sg"
    mock_outputs = {
        sg_id = "mock-alb-security-group"
    }
}

dependency "acm" {
    config_path = "../acm"
    mock_outputs = {
        certificate_arn = "arn:aws:acm:eu-west-1:123456789012:certificate/abcd1234-ef56-7890-abcd-1234ef567890"
    }
}

dependency "vpc" {
    config_path = "../vpc"
    mock_outputs = {
        vpc_id = "mockId"
        public_subnet_ids = ["s1", "s2"]
    }
}

dependency "ec2" {
    config_path = "../ec2"
    mock_outputs = {
        id = "ec2MockId"
    }
}

inputs = {
    create = true
    create_security_group = false

    region = local.env_values.region
    internal = false
    load_balancer_type = "application"
    name = "${local.env_values.project}-${local.env_values.environment}-alb"
    security_groups = [dependency.alb_sg.outputs.sg_id]
    subnets = dependency.vpc.outputs.public_subnet_ids
    tags = {
        Name        = "${local.env_values.project}-${local.env_values.environment}-alb"
        Environment = local.env_values.environment
        Project     = local.env_values.project
    }
    target_groups = {
        main = {
            name        = "${local.env_values.project}-${local.env_values.environment}-tg"
            port        = 80
            protocol    = "HTTP"
            vpc_id      = dependency.vpc.outputs.vpc_id
            target_type = "instance"
            create_attachment = true
            target_id = dependency.ec2.outputs.id
            health_check = {
                path = "/"
                port = "80"
                protocol = "HTTP"
                healthy_threshold = 3
                unhealthy_threshold = 3
                timeout = 5
                interval = 30
                matcher = "200-399"
            }
            tags = {
                Name        = "${local.env_values.project}-${local.env_values.environment}-tg"
                Environment = local.env_values.environment
                Project     = local.env_values.project
            }
        }
            backend = {
            name        = "${local.env_values.project}-${local.env_values.environment}-tg-backend"
            port        = 8080
            protocol    = "HTTP"
            vpc_id      = dependency.vpc.outputs.vpc_id
            target_type = "instance"
            create_attachment = true
            target_id = dependency.ec2.outputs.id
            health_check = {
                path                = "/swagger-ui/index.html"
                port                = "8080"
                protocol            = "HTTP"
                healthy_threshold   = 3
                unhealthy_threshold = 3
                timeout             = 5
                interval            = 30
                matcher             = "200-399"
            }
            tags = {
                Name        = "${local.env_values.project}-${local.env_values.environment}-tg"
                Environment = local.env_values.environment
                Project     = local.env_values.project
            }
        }
    }

    listeners = {
        http = {
            port = 80
            protocol = "HTTP"
            redirect = {
                port        = "443"
                protocol    = "HTTPS"
                status_code = "HTTP_301"
            }
        }
        https = {
            port         = 443
            protocol     = "HTTPS"
            ssl_policy   = "ELBSecurityPolicy-2016-08"
            certificate_arn = dependency.acm.outputs.certificate_arn
            forward = {
                target_group_key = "main"
            }
            rules = {
                backend_https = {
                    priority = 100
                    conditions = [
                        {
                            host_header = {
                            values = ["backend.kjeldsengame.com"]
                            }
                        }
                    ]
                    actions = [
                        {
                            forward = {
                                target_group_key = "backend"
                            }
                        }
                        
                    ]
                }
            }
        }
    }

    route53_records = {
        alb =  {
            zone_id = "Z0530348E6EUGD6L57W7"
            name = "kjeldsengame.com"
            type = "A"
        }
        backend_alb = {
            zone_id = "Z0530348E6EUGD6L57W7"
            name = "backend.kjeldsengame.com"
            type = "A"
        }
    } 
}