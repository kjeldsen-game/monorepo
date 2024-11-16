# Main VPC resource - provides isolated network environment
resource "aws_vpc" "main" {
  cidr_block           = var.vpc_cidr # Defines the IP range for the entire VPC
  enable_dns_hostnames = true         # Enables DNS hostnames for EC2 instances
  enable_dns_support   = true         # Enables DNS resolution in the VPC

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
  cidr_block        = cidrsubnet(var.vpc_cidr, 8, count.index + 1)             # Automatically calculate subnet ranges
  availability_zone = data.aws_availability_zones.available.names[count.index] # Spread across AZs

  tags = {
    Name        = "${var.project}-${var.environment}-public-${count.index + 1}"
    Environment = var.environment
    Project     = var.project
  }
}

# Private subnets - for resources that don't need direct internet access (like databases)
resource "aws_subnet" "private" {
  count             = 1
  vpc_id            = aws_vpc.main.id
  cidr_block        = cidrsubnet(var.vpc_cidr, 8, count.index + 2) # Offset by 2 to avoid public subnet ranges
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
