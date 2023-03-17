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

Send message to a topic (active sending mode, you can send multiple)
- `docker exec -it kafka kafka-console-producer --bootstrap-server localhost:9092 --topic my-topic`

Consume messages from a topic (active receiving mode, you can receive multiple)
- `docker exec kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic my-topic --from-beginning`

---

## Localstack

We are using Localstack to have a local development environment emulating AWS Cloud services. For more information, access https://github.com/localstack/localstack

You need to run aws configure to set the env variables needed to work. You can set any values, SDK when working on local will look for the configuration to have a valid format but it won't try a real authentication.

---

## Hands-on

Create the needed environment variables 
- M2_PATH
- CARGO_PATH

Run up the entire platform
- `docker compose up -d`