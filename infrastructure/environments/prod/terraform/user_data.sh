#!/bin/bash
# Install Docker
yum update -y
yum install -y docker
service docker start
usermod -a -G docker ec2-user

# Install AWS CLI
yum install -y aws-cli

# Data persistence 
curl -O https://fastdl.mongodb.org/tools/db/mongodb-database-tools-amazon2-x86_64-100.10.0.rpm
sudo yum install -y mongodb-database-tools-*-100.10.0.rpm

# Set password for ec2-user
echo "ec2-user:${ssh_password}" | chpasswd

# Allow password authentication
sed -i 's/PasswordAuthentication no/PasswordAuthentication yes/' /etc/ssh/sshd_config
service sshd restart

# Create environment file
cat << EOF > /home/ec2-user/.env
%{ for key, value in environment_vars ~}
${key}=${value}
%{ endfor ~}
EOF

# Login to ECR
aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 324037306405.dkr.ecr.eu-west-1.amazonaws.com

# Run the container
docker pull 324037306405.dkr.ecr.eu-west-1.amazonaws.com/main-service:latest
docker pull 324037306405.dkr.ecr.eu-west-1.amazonaws.com/beta-frontend:latest

docker run -d \
  --name main-service \
  --restart always \
  -p 8080:8080 \
  --env-file /home/ec2-user/.env \
  324037306405.dkr.ecr.eu-west-1.amazonaws.com/main-service:latest

docker run -d \
  --name beta-frontend \
  --restart always \
  -p 80:3000 \
  --env-file /home/ec2-user/.env \
  324037306405.dkr.ecr.eu-west-1.amazonaws.com/beta-frontend:latest