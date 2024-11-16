# Create a DocumentDB cluster
resource "aws_docdb_cluster" "main" {
  cluster_identifier     = "${var.project}-${var.environment}-docdb"
  engine                 = "docdb"
  master_username        = var.master_username
  master_password        = var.master_password
  db_subnet_group_name   = aws_docdb_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.docdb.id]
  skip_final_snapshot    = true # For development. In production, you might want this false

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
  instance_class     = var.instance_class # db.t3.medium is the smallest available

  tags = {
    Name        = "${var.project}-${var.environment}-docdb-${count.index + 1}"
    Environment = var.environment
    Project     = var.project
  }
}

# Create subnet group for DocumentDB
resource "aws_docdb_subnet_group" "main" {
  name       = "${var.project}-${var.environment}-docdb"
  subnet_ids = var.private_subnet_ids

  tags = {
    Name        = "${var.project}-${var.environment}-docdb"
    Environment = var.environment
    Project     = var.project
  }
}

# Create security group for DocumentDB
resource "aws_security_group" "docdb" {
  name_prefix = "${var.project}-${var.environment}-docdb-"
  vpc_id      = var.vpc_id

  # Allow inbound from backend security group
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
