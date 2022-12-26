# Libs

We will end up having multiples libraries within this folder and maybe even different libraries languages. The important thing to understand is that the current flow for a library is: 
1. code changes
2. upload artifact to AWS Code Artifact
3. a service will use the library as a dependency 

For this to work you need to configure the repositories (like a mvn repository) for both the service and the library.

---

Interesting reading for AWS Code Artifact MVN example: https://docs.aws.amazon.com/codeartifact/latest/ug/maven-mvn.html

Accessing AWS Code Artifact requires an auth token. In order to get the token you need to run following command (after `aws configure` command for a user with access)  
- `export CODEARTIFACT_AUTH_TOKEN='aws codeartifact get-authorization-token --domain kjeldsen --domain-owner 040156513434 --region eu-west-1 --query authorizationToken --output text'`
- your local code (or local-env setup) will fail to run up if you don't have the token in the env var
