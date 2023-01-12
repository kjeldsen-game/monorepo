# local-env

This repository is a quick way of having a development environment ready to work on local.

---

## AWS 
- Install aws-cli
    - https://docs.aws.amazon.com/es_es/cli/latest/userguide/getting-started-install.html
- Login: aws configure

---

## AWS ECR

Login to ECR to download Docker images
- `aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 040156513434.dkr.ecr.eu-west-1.amazonaws.com`

Update Docker images to latest version
- `docker compose pull`

---

## AWS Code Artifact

Login to Code Artifact to publish/download libraries
- `export CODEARTIFACT_AUTH_TOKEN='aws codeartifact get-authorization-token --domain kjeldsen --domain-owner 040156513434 --region eu-west-1 --query authorizationToken --output text'` (Mac)
- `export CODEARTIFACT_AUTH_TOKEN=$(aws codeartifact get-authorization-token --domain kjeldsen --domain-owner 040156513434 --region eu-west-1 --query authorizationToken --output text)` (Windows - Git Bash) 

---

## Kafka

Create topic
- `docker exec kafka  kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic my-topic`

List topics
- `docker exec kafka  kafka-topics --list --bootstrap-server localhost:9092`

## Hands-on

Create the needed environment variables 
- M2_PATH
- CARGO_PATH

Run up the entire platform
- `docker compose up -d`