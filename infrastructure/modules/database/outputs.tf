output "cluster_endpoint" {
  description = "The cluster endpoint"
  value       = aws_docdb_cluster.main.endpoint
}

output "reader_endpoint" {
  description = "The cluster reader endpoint"
  value       = aws_docdb_cluster.main.reader_endpoint
}

output "security_group_id" {
  description = "The security group ID for the DocumentDB cluster"
  value       = aws_security_group.docdb.id
} 
