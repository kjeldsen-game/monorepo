@echo off

set SSH_USER=ec2-user
set SSH_PASS=password123
set DB_HOST=kjeldsen-prod-docdb-1.cgznmelkvcds.eu-west-1.docdb.amazonaws.com
set DB_PORT=27017
set LOCAL_PORT=27017
set DB_NAME=admin
set DB_USER=kjeldsenprodusername
set DB_PASS=kjeldsenprodpassword
set EXPORT_DIR=.\mongo_backup
set S3_BUCKET=docdb-persistence
set SSH_KEY_PATH=.\kjeldsen-prod-key.pem

:loop
echo Select an option:
echo 1) Export data to S3
echo 2) Import data from S3 to DocumentDB
set /p CHOICE=Enter your choice: 

if "%CHOICE%" == "1" goto export_data
if "%CHOICE%" == "2" goto import_data
if "%CHOICE%" == "3" goto exit

:export_data
echo Starting SSH tunnel to %SSH_HOST%...
ssh -i %SSH_KEY_PATH% -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null %SSH_USER%@%SSH_HOST% "mongodump --uri 'mongodb://%DB_USER%:%DB_PASS%@%DB_HOST%:%LOCAL_PORT%' --out '%EXPORT_DIR%' --verbose && aws s3 cp %EXPORT_DIR% s3://%S3_BUCKET%/ --recursive"
if errorlevel 1 goto error
echo Data successfully exported to the S3 bucket.
goto loop

:import_data
echo Starting SSH tunnel to %SSH_HOST%...
ssh -i %SSH_KEY_PATH% -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null %SSH_USER%@%SSH_HOST% "aws s3 cp s3://%S3_BUCKET%/ %EXPORT_DIR%/ --recursive && mongorestore --uri 'mongodb://%DB_USER%:%DB_PASS%@%DB_HOST%:%LOCAL_PORT%' --dir '%EXPORT_DIR%' --verbose"
if errorlevel 1 goto error
echo Data successfully imported from S3 to DocumentDB.
goto loop

:error
echo Failed to execute command. Exiting.
exit /b 1

:exit
echo Exiting.
exit /b 0
