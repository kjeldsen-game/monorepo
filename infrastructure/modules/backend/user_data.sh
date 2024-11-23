#!/bin/bash
# Install Docker
yum update -y
yum install -y docker
service docker start
usermod -a -G docker ec2-user

# Install AWS CLI
yum install -y aws-cli

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
aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin ${ecr_repository_url}

# Run the container
docker pull ${docker_image}
docker run -d \
  --name ${container_name} \
  --restart always \
  -p ${container_external_port}:${container_internal_port} \
  --env-file /home/ec2-user/.env \
  ${docker_image} 