https://docs.aws.amazon.com/codeartifact/latest/ug/maven-mvn.html

export CODEARTIFACT_AUTH_TOKEN=`aws codeartifact get-authorization-token --domain kjeldsen --domain-owner 040156513434 --region eu-west-1 --query authorizationToken --output text`
