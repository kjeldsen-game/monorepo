#!/bin/bash

SSH_USER="ec2-user"                         
SSH_PASS="password123"                
DB_HOST="kjeldsen-prod-docdb-1.cgznmelkvcds.eu-west-1.docdb.amazonaws.com"
DB_PORT=27017
LOCAL_PORT=27017                          
DB_NAME="admin"
DB_USER="kjeldsenprodusername"
DB_PASS="kjeldsenprodpassword"
EXPORT_DIR="./mongo_backup"
S3_BUCKET="docdb-persistence"
SSH_KEY_PATH="./kjeldsen-prod-key.pem"

get_ssh_host() {
  SSH_HOST=$(aws ec2 describe-instances --query 'Reservations[0].Instances[0].PublicDnsName' --output text)
  if [ -z "$SSH_HOST" ]; then
    echo "Failed to retrieve the SSH host. Exiting."
    exit 1
  fi
}

# Function to Start SSH Tunnel
export_data() {
  get_ssh_host
  echo "Starting SSH tunnel to $SSH_HOST to export data..."

  ssh -i "$SSH_KEY_PATH" -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null \
  "$SSH_USER@$SSH_HOST" "mongodump --uri 'mongodb://$DB_USER:$DB_PASS@$DB_HOST:$LOCAL_PORT' --out '$EXPORT_DIR' --verbose \
  && ls -al && aws s3 cp $EXPORT_DIR s3://$S3_BUCKET/ --recursive"

#   sshpass -p "$SSH_PASS" ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null \
#     "$SSH_USER@$SSH_HOST" "mongodump --uri 'mongodb://$DB_USER:$DB_PASS@$DB_HOST:$LOCAL_PORT' --out '$EXPORT_DIR' --verbose \
#     && ls -al && aws s3 cp $EXPORT_DIR s3://$S3_BUCKET/ --recursive"


  if [ $? -eq 0 ]; then
    echo "Data successfully exported to the S3 bucket."
  else
    echo "Failed to export data to S3 bucket."
    exit 1
  fi
}

import_data() {

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

