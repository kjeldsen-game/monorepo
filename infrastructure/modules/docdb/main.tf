resource "aws_docdb_cluster" "default" {
  cluster_identifier     = "${var.project}-${var.environment}-docdb"
  engine                 = "docdb"
  master_username        = var.docdb_master_username
  master_password        = var.docdb_master_password
  db_subnet_group_name   = aws_docdb_subnet_group.main.name
  vpc_security_group_ids = var.vpc_security_group_ids
  skip_final_snapshot    = var.skip_final_snapshot # For development. In production, you might want this false

  # Use the parameter group to disable TLS
  db_cluster_parameter_group_name = "dev-parameter-group"

  tags = {
    Name        = "${var.project}-${var.environment}-docdb"
    Environment = var.environment
    Project     = var.project
  }
}

resource "aws_docdb_cluster_instance" "main" {
  count              = var.instance_count
  identifier         = "${var.project}-${var.environment}-docdb-${count.index + 1}"
  cluster_identifier = aws_docdb_cluster.main.id
  instance_class     = var.instance_class
  tags = {
    Name        = "${var.project}-${var.environment}-docdb-${count.index + 1}"
    Environment = var.environment
    Project     = var.project
  }
}

resource "aws_docdb_subnet_group" "main" {
  name       = "${var.project}-${var.environment}-docdb"
  subnet_ids = var.subnet_ids
  tags = {
    Name        = "${var.project}-${var.environment}-docdb"
    Environment = var.environment
    Project     = var.project
  }
}

resource "aws_security_group" "docdb" {
  name_prefix = "${var.project}-${var.environment}-docdb-"
  vpc_id      = var.vpc_id
  # Allow inbound from ec2 security group
  ingress {
    from_port       = 27017
    to_port         = 27017
    protocol        = "tcp"
    security_groups = var.allowed_security_groups
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