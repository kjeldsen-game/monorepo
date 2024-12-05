terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}
provider "aws" {
  alias   = "acm"
  region  = "eu-west-1"
  profile = "default"
}
provider "aws" {
  alias   = "route53"
  region  = "eu-west-1"
  profile = "default"
}
provider "aws" {
  region  = "eu-west-1"
  profile = "default"
}
##############################
######### Networking #########
##############################
# Main VPC resource - provides isolated network environment
resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true
  tags = {
    Name        = "${var.project}-${var.environment}-vpc"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_subnet" "public" {
  count             = 2
  vpc_id            = aws_vpc.main.id
  cidr_block        = cidrsubnet("10.0.0.0/16", 8, count.index + 1)
  availability_zone = data.aws_availability_zones.available.names[count.index]
  tags = {
    Name        = "${var.project}-${var.environment}-public-${count.index + 1}"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_subnet" "private" {
  count             = 2
  vpc_id            = aws_vpc.main.id
  cidr_block        = cidrsubnet("10.0.0.0/16", 8, count.index + 3)
  availability_zone = data.aws_availability_zones.available.names[count.index]
  tags = {
    Name        = "${var.project}-${var.environment}-private-${count.index + 1}"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id
  tags = {
    Name        = "${var.project}-${var.environment}-igw"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"                  # Route all external traffic
    gateway_id = aws_internet_gateway.main.id # through the internet gateway
  }
  tags = {
    Name        = "${var.project}-${var.environment}-public-rt"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_route_table_association" "public" {
  count          = 2
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}
data "aws_availability_zones" "available" {
  state = "available"
}
##############################
######### Database ###########
##############################
resource "aws_docdb_cluster" "main" {
  cluster_identifier     = "${var.project}-${var.environment}-docdb"
  engine                 = "docdb"
  master_username        = var.docdb_master_username
  master_password        = var.docdb_master_password
  db_subnet_group_name   = aws_docdb_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.docdb.id]
  skip_final_snapshot    = true # For development. In production, you might want this false
  # Use the parameter group to disable TLS
  db_cluster_parameter_group_name = "dev-parameter-group"
  tags = {
    Name        = "${var.project}-${var.environment}-docdb"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_docdb_cluster_instance" "main" {
  count              = 1
  identifier         = "${var.project}-${var.environment}-docdb-${count.index + 1}"
  cluster_identifier = aws_docdb_cluster.main.id
  instance_class     = "db.t3.medium"
  tags = {
    Name        = "${var.project}-${var.environment}-docdb-${count.index + 1}"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_docdb_subnet_group" "main" {
  name       = "${var.project}-${var.environment}-docdb"
  subnet_ids = aws_subnet.private[*].id
  tags = {
    Name        = "${var.project}-${var.environment}-docdb"
    Environment = var.environment
    Project     = var.project
  }
}
output "docdb_cluster_endpoint" {
  value       = aws_docdb_cluster.main.endpoint
  description = "The endpoint of the DocumentDB cluster."
}
resource "aws_security_group" "docdb" {
  name_prefix = "${var.project}-${var.environment}-docdb-"
  vpc_id      = aws_vpc.main.id
  # Allow inbound from ec2 security group
  ingress {
    from_port       = 27017
    to_port         = 27017
    protocol        = "tcp"
    security_groups = [aws_security_group.ec2.id]
  }
  # Allow all outbound traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name        = "${var.project}-${var.environment}-docdb"
    Environment = var.environment
    Project     = var.project
  }
}
##############################
########## Key Pair ##########
##############################
resource "tls_private_key" "ec2_key" {
  algorithm = "RSA"
  rsa_bits  = 2048
}
resource "aws_key_pair" "ec2_key" {
  key_name   = "${var.project}-${var.environment}-key"
  public_key = tls_private_key.ec2_key.public_key_openssh
}
resource "local_file" "private_key" {
  content         = tls_private_key.ec2_key.private_key_pem
  filename        = "${path.module}/${var.project}-${var.environment}-key.pem"
  file_permission = "0400"
}
output "ec2_private_key" {
  value       = tls_private_key.ec2_key.private_key_pem
  sensitive   = true
  description = "The private key for SSH access to the EC2 instance."
}
##############################
######### EC2 ################
##############################
# Get the latest Amazon Linux 2 AMI
data "aws_ami" "amazon_linux_2" {
  most_recent = true
  owners      = ["amazon"]
  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }
}
resource "aws_security_group" "ec2" {
  name_prefix = "${var.project}-${var.environment}-"
  vpc_id      = aws_vpc.main.id
  # Allow SSH access
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port       = 80
    to_port         = 80
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id] # ALB SG only
  }
  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id] # ALB SG only
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name        = "${var.project}-${var.environment}"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_iam_role" "ec2" {
  name = "${var.project}-${var.environment}"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}
# Add ECR permissions to the role
resource "aws_iam_role_policy" "ecr_policy" {
  name = "${var.project}-${var.environment}-ecr-policy"
  role = aws_iam_role.ec2.id
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ecr:GetAuthorizationToken",
          "ecr:BatchCheckLayerAvailability",
          "ecr:GetDownloadUrlForLayer",
          "ecr:BatchGetImage"
        ]
        Resource = "*"
      }
    ]
  })
}
resource "aws_iam_instance_profile" "ec2" {
  name = "${var.project}-${var.environment}"
  role = aws_iam_role.ec2.name
}
resource "aws_instance" "ec2" {
  ami                         = data.aws_ami.amazon_linux_2.id
  instance_type               = "t2.micro"
  subnet_id                   = aws_subnet.public[0].id
  vpc_security_group_ids      = [aws_security_group.ec2.id]
  iam_instance_profile        = aws_iam_instance_profile.ec2.name
  associate_public_ip_address = true                          # This enables public IP
  key_name                    = aws_key_pair.ec2_key.key_name # Dynamically generated key pair
  user_data = templatefile("${path.module}/user_data.sh", {
    ssh_password = var.ssh_password
    environment_vars = {
      # backend
      SPRING_PROFILES_ACTIVE        = "aws"
      SERVER_PORT                   = 8080
      ACTUATOR_SERVER_PORT          = 8081
      MONGO_HOST                    = aws_docdb_cluster.main.endpoint
      MONGO_PORT                    = 27017
      MONGO_USER                    = var.docdb_master_username
      MONGO_PASSWORD                = var.docdb_master_password
      MONGO_DATABASE                = "admin"
      MONGO_OPTIONS                 = "?replicaSet=rs0&readPreference=secondaryPreferred&retryWrites=false"
      ACCESS_TOKEN_VALIDITY_SECONDS = 86400
      PUBLIC_KEY                    = var.public_key
      PRIVATE_KEY                   = var.private_key
      # frontend
      NEXTAUTH_SECRET         = "Gd86qxgWNPJRBY3x56YmsZuyT-lZAV6CXafqxDJ2nw8P9jQZfocwm3338xfUlY7si6tAk9C4WPvxhQT1uUDSWQ"
      NEXT_AUTH_BACKEND_URL   = "http://main-service:8080/v1"
      NEXT_PUBLIC_BACKEND_URL = "http://main-service:8080/v1"
      NEXTAUTH_URL            = "http://beta-frontend:3000"
    }
  })
  tags = {
    Name        = "${var.project}-${var.environment}"
    Environment = var.environment
    Project     = var.project
  }
}
output "ec2_public_ip" {
  value = aws_instance.ec2.public_ip
}
# Create an Elastic IP for the EC2 instance
resource "aws_eip" "ec2" {
  instance = aws_instance.ec2.id
  vpc      = true
  tags = {
    Name        = "${var.project}-${var.environment}-eip"
    Environment = var.environment
    Project     = var.project
  }
}
##############################
########## ACM ###############
##############################
module "acm" {
  source  = "terraform-aws-modules/acm/aws"
  version = "~> 4.0"
  providers = {
    aws = aws.acm
  }
  domain_name = "kjeldsengame.com"
  subject_alternative_names = [
    "backend.kjeldsengame.com"
  ]
  validation_method      = "DNS"
  create_route53_records = false
  tags = {
    Name        = "${var.project}-${var.environment}-acm"
    Environment = var.environment
    Project     = var.project
  }
}
module "route53_records" {
  source  = "terraform-aws-modules/acm/aws"
  version = "~> 4.0"
  providers = {
    aws = aws.route53
  }
  create_certificate                        = false
  create_route53_records_only               = true
  validation_method                         = "DNS"
  distinct_domain_names                     = module.acm.distinct_domain_names
  zone_id                                   = "Z03138513K6V3S28YCMV7"
  acm_certificate_domain_validation_options = module.acm.acm_certificate_domain_validation_options
}
##############################
########## ALB ###############
##############################
resource "aws_security_group" "alb" {
  name_prefix = "${var.project}-${var.environment}-alb-"
  vpc_id      = aws_vpc.main.id
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name        = "${var.project}-${var.environment}-alb-sg"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_lb" "main" {
  name               = "${var.project}-${var.environment}-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = aws_subnet.public[*].id
  tags = {
    Name        = "${var.project}-${var.environment}-alb"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_lb_target_group" "main" {
  name        = "${var.project}-${var.environment}-tg"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = aws_vpc.main.id
  target_type = "instance"
  health_check {
    path                = "/"
    port                = "80"
    protocol            = "HTTP"
    healthy_threshold   = 3
    unhealthy_threshold = 3
    timeout             = 5
    interval            = 30
    matcher             = "200-299"
  }
  tags = {
    Name        = "${var.project}-${var.environment}-tg"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_lb_target_group_attachment" "main" {
  target_group_arn = aws_lb_target_group.main.arn
  target_id        = aws_instance.ec2.id
  port             = 80
}
resource "aws_lb_target_group" "backend" {
  name        = "${var.project}-${var.environment}-tg-backend"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = aws_vpc.main.id
  target_type = "instance"
  health_check {
    path                = "/swagger-ui/index.html"
    port                = "8080"
    protocol            = "HTTP"
    healthy_threshold   = 3
    unhealthy_threshold = 3
    timeout             = 5
    interval            = 30
    matcher             = "200-299"
  }
  tags = {
    Name        = "${var.project}-${var.environment}-tg-backend"
    Environment = var.environment
    Project     = var.project
  }
}
resource "aws_lb_target_group_attachment" "backend" {
  target_group_arn = aws_lb_target_group.backend.arn
  target_id        = aws_instance.ec2.id
  port             = 8080
}
resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.main.arn
  port              = 80
  protocol          = "HTTP"
  default_action {
    type = "redirect"
    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
  }
}
resource "aws_lb_listener" "https" {
  load_balancer_arn = aws_lb.main.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = module.acm.acm_certificate_arn
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.main.arn
  }
}
resource "aws_lb_listener_rule" "backend_https" {
  listener_arn = aws_lb_listener.https.arn
  priority     = 100
  condition {
    host_header {
      values = ["backend.kjeldsengame.com"]
    }
  }
  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.backend.arn
  }
}
##############################
########## DNS ###############
##############################
resource "aws_route53_record" "alb" {
  zone_id = "Z03138513K6V3S28YCMV7"
  name    = "kjeldsengame.com"
  type    = "A"
  alias {
    name                   = aws_lb.main.dns_name
    zone_id                = aws_lb.main.zone_id
    evaluate_target_health = true
  }
}
resource "aws_route53_record" "backend_alb" {
  zone_id = "Z03138513K6V3S28YCMV7"
  name    = "backend.kjeldsengame.com"
  type    = "A"
  alias {
    name                   = aws_lb.main.dns_name
    zone_id                = aws_lb.main.zone_id
    evaluate_target_health = true
  }
}
