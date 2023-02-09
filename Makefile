ENV := local
DOCKER_CONTAINER := kjeldsen
NETWORK := kjeldsen_network
PROJECT_NAME := Kjeldsen

DOCKER_SERVICES := local-mongodb auth-service gateway-service player-service
MAVEN_PROJECTS := $(shell find ./services -type f -mindepth 2 -maxdepth 2 -name "pom.xml" -exec dirname {} \; | xargs -I {} basename {})


# Output colors
NORMAL := \033[0m
CYAN   := \033[36m
YELLOW := \033[33m
BLUE   := \033[34m
RED    := \033[31m

M2_PATH := $(shell echo $$M2_PATH)/settings.xml

# Detect docker executable and when called inside container
DOCKER_EXEC := "$(shell which docker docker.exe | head -n1)"

# Code artifact token
CODEARTIFACT_AUTH_TOKEN := $(shell aws codeartifact get-authorization-token --domain kjeldsen --domain-owner 040156513434 --region eu-west-1 --query authorizationToken --output text)

.DEFAULT_GOAL = help
.SUFFIXES:
ifndef VERBOSE
.SILENT:
endif

MSG_SEPARATOR := "*********************************************************************"
MSG_IDENT := "    "

.PHONY: help
help: ## Show command list
	@echo ""
	@echo " > \033[1;37m$(PROJECT_NAME)\033[0m commands"
	@echo ""
	@echo You can use the following commands:
	# Parses current makefile looking for targets
	# Comments after the command are used as description
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN { FS = ":.*?## " }; { printf " - \033[1;36m%s:\033[0m %s\n", $$1, $$2 }'
	@echo

######################################################################
########################   BASIC    #################################
######################################################################
.PHONY: up
up: backend frontend ## Start the application
	@echo "\n\n${MSG_SEPARATOR}\n\n Now 🤘 APPLICATION is started.\n\n${MSG_SEPARATOR}\n\n"

.PHONY: down
down: ## Stop all backend services
	docker-compose -f ./docker-compose.yml down -t0

.PHONY: build
build: ## Build all backend services
	echo "\n\n${MSG_SEPARATOR}\n\n Building 🤘 BACKEND.\n\n${MSG_SEPARATOR}\n\n"
	$(MAKE) .build-maven-projects

.PHONY: backend
backend: build ## Start backend services
	@echo "\n\n${MSG_SEPARATOR}\n\n Starting up the 🤘 BACKEND.\n\n${MSG_SEPARATOR}\n\n"
	@docker compose -f ./docker-compose.yml up --build -t0 -d $(DOCKER_SERVICES)
	@echo "\n\n${MSG_SEPARATOR}\n\n Now 🤘 BACKEND is started.\n\n"
	@echo "Go to http://localhost:8080/actuator/health. Expect to see {\"status\",\"up\"}.\n\n"
	@echo "Go to http://localhost:8080/swagger-ui. To see the documentation API.\n\n${MSG_SEPARATOR}\n\n"

.PHONY: frontend
frontend: ## Start frontend services
	@echo "\n\n${MSG_SEPARATOR}\n\n Starting up the 🤘 FRONTEND.\n\n${MSG_SEPARATOR}\n\n"
	@cd ./frontends/beta-frontend && npm install && npm run dev

.PHONY: .build-maven-projects
.build-maven-projects: ## Build all maven based services
	export CODEARTIFACT_AUTH_TOKEN
	for service in $(MAVEN_PROJECTS); do \
		echo "${MSG_IDENT}Building $$service"; \
		if [ -f ./services/$$service/.mvn/settings.xml ]; then \
			mvn -s ./services/$$service/.mvn/settings.xml -f ./services/$$service/pom.xml clean package -DskipTests; \
		else \
			mvn -f ./services/$$service/pom.xml clean package -DskipTests; \
		fi; \
	done;