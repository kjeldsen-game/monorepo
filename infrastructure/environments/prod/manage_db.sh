#!/bin/bash

SSH_USER="ec2-user"                         
DB_PORT=27017
LOCAL_PORT=27017                          
DB_NAME="admin"
DB_USER="kjeldsenprodusername"
DB_PASS="kjeldsenprodpassword"
EXPORT_DIR="./mongo_backup"
S3_BUCKET="db-persistence"
SSH_KEY_PATH="./kjeldsen-prod-key.pem"

get_db_host() {
  DB_HOST=$(
    aws rds describe-db-clusters \
    --db-cluster-identifier "$DOCDB_CLUSTER_IDENTIFIER" \
    --query "DBClusters[0].Endpoint" \
    --output text)
  if [ -z "$DB_HOST" ]; then
    echo "Failed to retrieve the DB host. Exiting."
    exit 1
  fi
}

get_ssh_host() {
  SSH_HOST=$(aws ec2 describe-instances \
    --query "Reservations[*].Instances[?State.Name=='running'].PublicDnsName" \
    --output text)
  if [ -z "$SSH_HOST" ]; then
    echo "Failed to retrieve the SSH host. Exiting."
    exit 1
  fi
}

# Function to Start SSH Tunnel
export_data() {
  get_ssh_host
  get_db_host
  echo "Starting SSH tunnel to $SSH_HOST to export data..."

  ssh -i "$SSH_KEY_PATH" -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null \
  "$SSH_USER@$SSH_HOST" "mongodump --uri 'mongodb://$DB_USER:$DB_PASS@$DB_HOST:$LOCAL_PORT' --out '$EXPORT_DIR' --verbose \
  && ls -al && aws s3 cp $EXPORT_DIR s3://$S3_BUCKET/ --recursive"

  if [ $? -eq 0 ]; then
    echo "Data successfully exported to the S3 bucket."
  else
    echo "Failed to export data to S3 bucket."
    exit 1
  fi
}

import_data() {
  get_db_host
  get_ssh_host
  echo "Starting SSH tunnel to $SSH_HOST to import data..."
   ssh -i "$SSH_KEY_PATH" -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null \
    "$SSH_USER@$SSH_HOST" "aws s3 cp s3://$S3_BUCKET/ $EXPORT_DIR/ --recursive \
    && mongorestore --uri 'mongodb://$DB_USER:$DB_PASS@$DB_HOST:$LOCAL_PORT' --dir '$EXPORT_DIR' --verbose"


  if [ $? -eq 0 ]; then
    echo "Data successfully imported from S3 to DocumentDB."
  else
    echo "Failed to import data from S3 to DocumentDB."
    exit 1
  fi
}

if [ -f "kjeldsen-prod-key.pem" ]; then
    echo "File kjeldsen-prod-key.pem exists. Removing it..."
    rm -rf kjeldsen-prod-key.pem
else
    echo "File kjeldsen-prod-key.pem does not exist. Retrieving secret and creating the file..."
fi

aws secretsmanager get-secret-value \
    --secret-id "kjeldsen-prod-pem" \
    --query SecretString \
    --output text > kjeldsen-prod-key.pem
chmod 400 ./kjeldsen-prod-key.pem

# Main Menu
echo "Select an option:"
echo "1) Export data to S3"
echo "2) Import data from S3 to DocumentDB"
read -p "Enter your choice: " CHOICE

case $CHOICE in
  1)
    export_data
    ;;
  2)
    import_data
    ;;
  *)
    echo "Invalid choice. Exiting."
    exit 1
    ;;
esac


