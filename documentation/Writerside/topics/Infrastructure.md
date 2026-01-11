# Infrastructure

This section provides an overview of the infrastructure components required to run, build, and analyze the project. 
It describes both the local development setup and the tools used to maintain code quality, monitor performance, and 
ensure smooth operation across different environments.

## Local Environment

The local environment provides developers with the necessary setup to run and test the application on their own machines. 
All services required for development are containerized using Docker, ensuring a consistent environment across different machines.

In this section, we describe how to start up the application and perform local code quality scans using SonarQube. Currently, 
we focus on local execution, as cloud-based SonarQube scanning is not yet supported. The following subsections will guide 
you through running the application locally and performing a Sonar scan.

### Kjeldsen Application
To setup and run up Kjeldsen locally with **Docker** follow these steps:
1. Go to the directory with the docker-compose files `/local-env`
2. Start up the Kjeldsen application docker compose file ```docker compose up -d --build```
3. Application spin up 3 containers:
   - MongoDB database
   - Main service
   - Beta Frontend
4. Access the Beta Frontend at `http://localhost:3000`

### MongoDB
To access the database and see collections follow these steps:
1. Make sure you have downloaded and installed **Studio3T** and **MongoDB** container is running
2. Start up the Studio3T
3. Click on the **Connect** on left top corner, after window pop up click **New Connection**
4. Once **New Connection** window pop up, paste `mongodb://admin:root@localhost:27017/admin` as URI and click **Next**
5. Then write out **Connection Name** ex. **Kjeldsen_local**,
6. Optionally you can click on **Test Connection**, if status is OK connection is well configured
7. Click on **Save** button

### SonarQube
To setup the **SonarQube** locally follow these steps:
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

## Prod Environment

The production environment hosts the live application accessible to end users. The infrastructure is deployed in the 
cloud using **AWS**, with services running on **EC2 instances** using Docker Compose to start both the frontend and 
backend services. For persistent storage, the application relies on **DocumentDB** as the database.

### Turning up Environment
1. Navigate to the production Terraform directory:
   ```bash
   cd infrastructure/environments/prod/terraform
   ```
2. Apply the Terraform configuration to provision the infrastructure:
   ```bash
   terraform apply --var-file=prod.tfvars
   ```
3. Follow the prompts and confirm any actions by typing ```yes```.
4. Terraform will start spinning up the infrastructure. This process typically takes 5–10 minutes.
5. Once completed, the application will be accessible via the web at:
   ```
   https://kjeldsengame.com
   ```

### Turning down Environment
1. Navigate to the production Terraform directory:
   ```bash
   cd infrastructure/environments/prod/terraform
   ```
2. Destroy the infrastructure using Terraform:
   ```bash
   terraform destroy --var-file=prod.tfvars
   ```
3. Confirm the destruction by typing ```yes```.
4. The teardown process typically takes 10–15 minutes, after which all resources will be removed.

### Cloud Infrastructure

Our production infrastructure is hosted on **AWS** and consists of several key components that work together to run the application.

- **VPC (Virtual Private Cloud)** – a logically isolated network where all services run.
- **Public Subnet** – contains the **ALB (Application Load Balancer)** and **EC2 instance**, which run the frontend and backend services via Docker Compose.
- **Private Subnet** – contains the **DocumentDB Cluster**, which provides persistent storage for the backend.
- **Route53 DNS** – routes the domains `kjeldsengame.com` and `backend.kjeldsengame.com` to the ALB.
- **Internet Users** – access the application through DNS and the ALB.

Below is a visual representation of the infrastructure, showing how the components are placed inside the VPC and how they communicate:


```mermaid
graph LR
    %% Users
    Internet(("Internet Users"))

    %% DNS
    DNSMain["Route53: kjeldsengame.com"]
    DNSBackend["Route53: backend.kjeldsengame.com"]

    %% VPC
    subgraph VPC["VPC"]
        
        %% Public Subnets
        subgraph PublicSubnets["Public Subnet"]
            ALB["ALB HTTP/HTTPS"]
            
            EC2["EC2"]
            subgraph EC2_Services["Services"]
                Frontend["Frontend Service"]
                Backend["Backend Service"]
            end
        end

        %% Private Subnets
        subgraph PrivateSubnets["Private Subnet"]
            DocDB["DocumentDB Cluster"]
        end
    end
    
    %% Connections
    Internet --> DNSMain
    Internet --> DNSBackend

    DNSMain --> ALB
    DNSBackend --> ALB

    ALB --> Frontend
    ALB --> Backend

    Backend --> DocDB
```
