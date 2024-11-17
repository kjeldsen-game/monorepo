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

# Download DocumentDB certificate
mkdir -p /home/ec2-user/certs
wget -O /home/ec2-user/certs/rds-combined-ca-bundle.pem https://s3.amazonaws.com/rds-downloads/rds-combined-ca-bundle.pem

#Add the DocumentDB certificate to a Java truststore
yum install -y java-17-amazon-corretto
keytool -import -trustcacerts \
  -file /home/ec2-user/certs/rds-combined-ca-bundle.pem \
  -alias docdb \
  -keystore /home/ec2-user/certs/truststore.jks \
  -storepass ${ssh_password} -noprompt

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
  --name backend \
  --restart always \
  -p ${container_external_port}:${container_internal_port} \
  -v /home/ec2-user/certs:/certs \
  --env-file /home/ec2-user/.env \
  ${docker_image} 