resource "aws_instance" "default" {
  ami                         = data.aws_ami.amazon_linux_2.id
  instance_type               = var.instance_type
  subnet_id                   = var.subnet_id
  vpc_security_group_ids      = var.vpc_security_group_ids
  iam_instance_profile        = var.iam_instance_profile
  associate_public_ip_address = var.associate_public_ip_address
  key_name                    = var.aws_key_pair

  user_data = templatefile("${path.module}/user_data.sh", {
    ssh_password     = var.ssh_password
    environment_vars = merge(var.environment_vars, {
      MONGO_HOST   = aws_docdb_cluster.main.endpoint
      MONGO_USER   = var.docdb_master_username
      MONGO_PASSWORD = var.docdb_master_password
      PUBLIC_KEY   = var.public_key
      PRIVATE_KEY  = var.private_key
    })
  })
}

resource "aws_eip" "default" {
  instance = aws_instance.ec2.id
  vpc      = true
  tags = {
    Name        = "${var.project}-${var.environment}-eip"
    Environment = var.environment
    Project     = var.project
  }
}