# Demo

This project contains a demo of the game for feedback and testing purposes.

## Requirements

- Java 17 (if you opened the project in IntelliJ, it will prompt you to download it)
- Docker (skip next section if you don't want to use Docker)

## Installation

### With Docker

From your terminal:

1. Build the project
2. Build the Docker image
3. Start the Docker container

```console
./mvnw -Dmaven.test.skip=true install && \
docker build . -t kjeldsen-game && \
docker compose up -d
```

Alternatively run these commands directly from your IDE by opening these files and clicking the
green play button (or similar):

1. [Makefile](./Makefile)
2. [Dockerfile](./Dockerfile)
3. [docker compose](./docker-compose.yml)

View the application at

http://localhost:8083/

### With IntelliJ

Run the `main` method
in [MatchServiceApplication.java](./src/main/java/com/kjeldsen/match/MatchServiceApplication.java)
by clicking the green play button.

http://localhost:8083/
