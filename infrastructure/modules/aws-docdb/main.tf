resource "aws_docdb_cluster" "default" {
  cluster_identifier     = "${var.docdb_identifier}-docdb"
  engine                 = "docdb"
  master_username        = var.docdb_username
  master_password        = var.docdb_password

  db_subnet_group_name   = aws_docdb_subnet_group.default.name
  vpc_security_group_ids = var.vpc_security_group_ids
  skip_final_snapshot    = var.skip_final_snapshot # For development. In production, you might want this false

  db_cluster_parameter_group_name = "dev-parameter-group"
  tags = {
    Name        = "${var.docdb_identifier}-docdb"
    Environment = var.environment
    Project     = var.project
  }
}

resource "aws_docdb_cluster_instance" "default" {
  count              = var.instance_count
  identifier         = "${var.docdb_identifier}-docdb-${count.index + 1}"
  cluster_identifier = aws_docdb_cluster.default.id
  instance_class     = var.instance_class
  tags = {
    Name        = "${var.docdb_identifier}-docdb-${count.index + 1}"
    Environment = var.environment
    Project     = var.project
  }
}

resource "aws_docdb_subnet_group" "default" {
  name       = "${var.docdb_identifier}-docdb"
  subnet_ids = var.subnet_ids
  tags = {
    Name        = "${var.docdb_identifier}-docdb"
    Environment = var.environment
    Project     = var.project
  }
}