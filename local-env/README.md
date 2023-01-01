# local-env

This repository is a quick way of having a development environment ready to work on local.

---

## AWS 
- Install aws-cli
- Login: aws configure

---

## AWS ECR

Login to ECR to download Docker images
- `aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 040156513434.dkr.ecr.eu-west-1.amazonaws.com`

Update Docker images to latest version
- `docker compose pull`

Run up the entire platform
- `docker compose up -d`

---

## AWS Code Artifact

Login to Code Artifact to publish/download libraries
- `export CODEARTIFACT_AUTH_TOKEN='aws codeartifact get-authorization-token --domain kjeldsen --domain-owner 040156513434 --region eu-west-1 --query authorizationToken --output text'`

---

## Kafka

- `docker exec kafka  kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic my-topic`
- `docker exec kafka  kafka-topics --list --bootstrap-server localhost:9092`
