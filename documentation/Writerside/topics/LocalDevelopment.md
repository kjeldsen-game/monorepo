# Local development

This repository is a quick way of having a development environment ready to work on local.

## Requirements
- Docker
- Terminal
- Studio3T (MongoDB client)

## Setting up environment
To setup and run up Kjeldsen locally w Docker follow these steps:
1. Open the `/local-env/config/beta-frontend.env` file and in **Docker run** section uncomment environment variables
and comment the ones under **Ngrok Hosting**
2. Once config file set up properly go back to the `/local-env` directory and run docker compose file
3. `docker compose up -d --build`

## MongoDB
To access the database and see collections follow these steps:
1. Make sure you have downloaded and installed Studio3T and MongoDB container is running
2. Start up the Studio3T
3. Click on the **Connect** on left top corner, after window pop up click **New Connection**
4. Once **New Connection** window pop up, paste `mongodb://admin:root@localhost:27017/admin` as URI and click **Next**
5. Then write out **Connection Name** ex. **Kjeldsen_local**,
6. Optionally you can click on **Test Connection**, if status is OK connection is well configured
7. Click on **Save** button

## SonarQube 
To setup the SonarQube locally follow these steps:
1. Go to the directory with docker-compose files`/local-env`
2. Start up the SonarQube docker compose file ```docker compose -f docker-compose_sonar.yml up -d```
3. Login into the SonarQube with credentials defined in docker-compose file, after first login the password change will
be required
4. Click on the "Manually" card in bottom to add project
   ![SonarQube UI](../images/sonarqube_ui.png) {width="500"}
5. Fill the fields as showed on image
   ![SonarQube UI](../images/sonarqube_create_project.png) {width="500"}
6. Click on the "Locally" card and generate the local token for authentication
   ![SonarQube UI](../images/sonarqube_local_token.png) {width="500"}
7. Once you finished up the configuration in SonarQube navigate to `/services/main-service`
8. Run the command to execute sonar scan: 
   ``` 
   mvn verify sonar:sonar \
    -Dsonar.projectKey=main-service \
    -Dsonar.host.url=http://localhost:9000 \
    -Dsonar.login=<use_your_token_value>
   ```
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