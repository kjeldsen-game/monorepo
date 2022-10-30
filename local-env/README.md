## local-env

This repository is a quick way of having a development environment ready to work on local.

Login to ECR to download Docker images
- ```aws configure```
- ```aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 040156513434.dkr.ecr.eu-west-1.amazonaws.com```