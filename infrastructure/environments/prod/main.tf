terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

provider "aws" {
  region  = "eu-west-1"
  profile = "kjeldsen"
}

##############################
######### Networking #########
##############################

# Main VPC resource - provides isolated network environment
resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/16" # Defines the IP range for the entire VPC
  enable_dns_hostnames = true          # Enables DNS hostnames for EC2 instances
  enable_dns_support   = true          # Enables DNS resolution in the VPC

  tags = {
    Name        = "${var.project}-${var.environment}-vpc"
    Environment = var.environment
    Project     = var.project
  }
}

# Public subnets - for resources that need direct internet access (like load balancers)
resource "aws_subnet" "public" {
  count             = 1
  vpc_id            = aws_vpc.main.id
  cidr_block        = cidrsubnet("10.0.0.0/16", 8, count.index + 1)            # Automatically calculate subnet ranges
  availability_zone = data.aws_availability_zones.available.names[count.index] # Spread across AZs

  tags = {
    Name        = "${var.project}-${var.environment}-public-${count.index + 1}"
    Environment = var.environment
    Project     = var.project
  }
}

# Private subnets - for resources that don't need direct internet access (like databases)
resource "aws_subnet" "private" {
  count             = 2
  vpc_id            = aws_vpc.main.id
  cidr_block        = cidrsubnet("10.0.0.0/16", 8, count.index + 2) # Offset by 2 to avoid public subnet ranges
  availability_zone = data.aws_availability_zones.available.names[count.index]

  tags = {
    Name        = "${var.project}-${var.environment}-private-${count.index + 1}"
    Environment = var.environment
    Project     = var.project
  }
}

# Internet Gateway - enables communication between VPC and internet
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name        = "${var.project}-${var.environment}-igw"
    Environment = var.environment
    Project     = var.project
  }
}

# Route table for public subnets - defines routing rules
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

# Associates the public subnets with the public route table
resource "aws_route_table_association" "public" {
  count          = 1 # One association per public subnet
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

# Data source to get available AZs in the region
data "aws_availability_zones" "available" {
  state = "available"
}

##############################
######### Database ###########
##############################

# Create a DocumentDB cluster
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

# Create a DocumentDB instance within the cluster
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

# Create subnet group for DocumentDB
resource "aws_docdb_subnet_group" "main" {
  name       = "${var.project}-${var.environment}-docdb"
  subnet_ids = aws_subnet.private[*].id

  tags = {
    Name        = "${var.project}-${var.environment}-docdb"
    Environment = var.environment
    Project     = var.project
  }
}

# Create security group for DocumentDB
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

# Security group for the EC2 instance
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

  # Allow inbound traffic on your application port
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
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
    Name        = "${var.project}-${var.environment}"
    Environment = var.environment
    Project     = var.project
  }
}

# IAM role for EC2
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

# IAM instance profile
resource "aws_iam_instance_profile" "ec2" {
  name = "${var.project}-${var.environment}"
  role = aws_iam_role.ec2.name
}

# EC2 instance
resource "aws_instance" "ec2" {
  ami                         = data.aws_ami.amazon_linux_2.id
  instance_type               = "t2.micro"
  subnet_id                   = aws_subnet.public[0].id
  vpc_security_group_ids      = [aws_security_group.ec2.id]
  iam_instance_profile        = aws_iam_instance_profile.ec2.name
  associate_public_ip_address = true # This enables public IP

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
      APP_ENV                 = "production"
    }
  })

  tags = {
    Name        = "${var.project}-${var.environment}"
    Environment = var.environment
    Project     = var.project
  }
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

resource "aws_route53_record" "ec2" {
  zone_id = "Z03138513K6V3S28YCMV7"
  name    = "www.kjeldsengame.com"
  type    = "A"
  ttl     = "60"
  records = [aws_eip.ec2.public_ip]
}
