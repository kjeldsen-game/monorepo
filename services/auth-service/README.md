http://localhost:8081/swagger-ui/index.html

mvn spring-boot:run -f infrastructure

docker build -t 040156513434.dkr.ecr.eu-west-1.amazonaws.com/auth-service

docker push 040156513434.dkr.ecr.eu-west-1.amazonaws.com/auth-service:latest

