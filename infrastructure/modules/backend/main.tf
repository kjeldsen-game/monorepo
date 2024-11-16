# Get the latest Amazon Linux 2 AMI
data "aws_ami" "amazon_linux_2" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }
}

# Security group for the EC2 instance
resource "aws_security_group" "backend" {
  name_prefix = "${var.project}-${var.environment}-backend-"
  vpc_id      = var.vpc_id

  # Allow SSH access
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Allow inbound traffic on your application port
  ingress {
    from_port   = var.container_external_port
    to_port     = var.container_internal_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Allow all outbound traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "${var.project}-${var.environment}-backend"
    Environment = var.environment
    Project     = var.project
  }
}

# IAM role for EC2
resource "aws_iam_role" "backend" {
  name = "${var.project}-${var.environment}-backend"

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
  role = aws_iam_role.backend.id

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

# IAM instance profile
resource "aws_iam_instance_profile" "backend" {
  name = "${var.project}-${var.environment}-backend"
  role = aws_iam_role.backend.name
}

# EC2 instance
resource "aws_instance" "backend" {
  ami                         = data.aws_ami.amazon_linux_2.id
  instance_type               = var.instance_type
  subnet_id                   = var.subnet_id
  vpc_security_group_ids      = [aws_security_group.backend.id]
  iam_instance_profile        = aws_iam_instance_profile.backend.name
  associate_public_ip_address = true # This enables public IP

  user_data = templatefile("${path.module}/user_data.sh", {
    ecr_repository_url      = var.ecr_repository_url
    docker_image            = var.docker_image
    container_external_port = var.container_external_port
    container_internal_port = var.container_internal_port
    environment_vars        = var.environment_vars
    ssh_password            = var.ssh_password
  })

  tags = {
    Name        = "${var.project}-${var.environment}-backend"
    Environment = var.environment
    Project     = var.project
  }
}
