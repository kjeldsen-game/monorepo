output "cluster_id" {
  value = aws_docdb_cluster.default.id
}

output "cluster_endpoint" {
  value = aws_docdb_cluster.default.endpoint
}