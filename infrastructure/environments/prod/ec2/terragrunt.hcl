terraform {
    source = "git::https://github.com/terraform-aws-modules/terraform-aws-ec2-instance.git?ref=v6.0.0"
}

locals {
    env_values = yamldecode(file(find_in_parent_folders("env_values.yaml")))
    kjeldsen_secrets = jsondecode(
        run_cmd("aws","secretsmanager","get-secret-value","--secret-id", "prod/kjeldsen-app-secret","--query", "SecretString","--output", "text")
    )
}

include "parent" {
    path = find_in_parent_folders("env_configs.hcl")
}

dependency "iam" {
    config_path = "./iam"
    mock_outputs = {
        instance_profile_name = "mock-instance-profile"
    }
}

dependency "key_pair" {
    config_path = "./key-pair"
    mock_outputs = {
        key_name = "mock-key-name"
    }
}

dependency "vpc" {
    config_path = "../vpc"
    mock_outputs = {
        public_subnet_ids = ["subnet-12345678", "subnet-87654321"]
    }
}

dependency "ec2_sg" {
    config_path = "./sg"
    mock_outputs = {
        sg_id = "mock-security-group"
    }
}

dependency "docdb" {
    config_path = "../docdb"
    mock_outputs = {
        cluster_endpoint = "mock-docdb-endpoint"
    }
}

inputs = {
    create = true
    create_eip = true
    create_security_group = false

    ami = "amzn2-ami-hvm-2.0.20250929.2-x86_64-gp2" 
    instance_type = "t3.small"
    subnet_id = dependency.vpc.outputs.public_subnet_ids[0]
    vpc_security_group_ids = [dependency.ec2_sg.outputs.sg_id]
    iam_instance_profile = dependency.iam.outputs.instance_profile_name
    associate_public_ip_address = true
    key_name = dependency.key_pair.outputs.key_name
    user_data = templatefile("${get_terragrunt_dir()}/user_data.sh", {
        ssh_password = local.kjeldsen_secrets.ssh_password
        environment_vars = {
            # backend
            SPRING_PROFILES_ACTIVE        = "aws"
            SERVER_PORT                   = 8080
            ACTUATOR_SERVER_PORT          = 8081
            MONGO_HOST                    = dependency.docdb.outputs.cluster_endpoint
            MONGO_PORT                    = 27017
            MONGO_USER                    = local.kjeldsen_secrets.docdb_username
            MONGO_PASSWORD                = local.kjeldsen_secrets.docdb_password
            MONGO_DATABASE                = "admin"
            MONGO_OPTIONS                 = "?replicaSet=rs0&readPreference=secondaryPreferred&retryWrites=false"
            ACCESS_TOKEN_VALIDITY_SECONDS = 86400
            PUBLIC_KEY                    = local.kjeldsen_secrets.public_key
            PRIVATE_KEY                   = local.kjeldsen_secrets.private_key
            # frontend
            NEXTAUTH_SECRET         = "Gd86qxgWNPJRBY3x56YmsZuyT-lZAV6CXafqxDJ2nw8P9jQZfocwm3338xfUlY7si6tAk9C4WPvxhQT1uUDSWQ"
            NEXT_AUTH_BACKEND_URL   = "http://main-service:8080/v1"
            NEXT_PUBLIC_BACKEND_URL = "http://main-service:8080/v1"
            NEXTAUTH_URL            = "https://kjeldsengame.com"
        }
    })
    
    tags = {
        Name        = "${local.env_values.project}-${local.env_values.environment}-eip"
        Environment = local.env_values.environment
        Project     = local.env_values.project
    }
}