# Main Service

## MVN commands

- `mvn spring-boot:run -f infrastructure`

## Docker commands

- `docker build -t 040156513434.dkr.ecr.eu-west-1.amazonaws.com/main-service`
- `docker push 040156513434.dkr.ecr.eu-west-1.amazonaws.com/main-service:latest`

## Open API

- http://localhost:15001/swagger-ui/index.html

## Generate public and private keys to sign tokens

- openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
- openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in private_key.pem -out private_key_pkcs8.pem
- openssl rsa -pubout -in private_key_pkcs8.pem -out public_key.pem

## Verify token in jwt.io

set the algorithm to HS512
