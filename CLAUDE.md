# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

Kjeldsen is a football (soccer) management game. This monorepo contains:
- `services/main-service/` — Java/Spring Boot backend (multi-module Maven project)
- `frontends/beta-frontend/` — Next.js frontend
- `local-env/` — Docker Compose files for local development
- `documentation/` — Writerside docs (game mechanics, architecture, security)

## Architecture

### Backend: Hexagonal Architecture + CQRS + Event Sourcing

The backend follows strict hexagonal architecture. Each domain area is split into modules:
- `{domain}-domain` — entities, repository interfaces, domain exceptions
- `{domain}-application` — use cases (one class per use case)
- `{domain}-adapter-rest` — REST controllers (OpenAPI-generated delegates)
- `{domain}-adapter-persistence` — MongoDB repository implementations
- `{domain}-adapter-listener` — Spring event listeners
- `{domain}-adapter-quartz` — scheduled jobs (Quartz)

Domain areas: `auth`, `player`, `match`, `market`, plus shared `notification`, `lib-pub-sub`, `lib-client`, `lib-api`.

The `infrastructure` module contains the Spring Boot entry point (`ServiceApplication`) and cross-cutting config.

**Use cases** are the only entry point to business logic. Adapters call use cases; use cases call domain and repository interfaces. Use cases never depend on adapter modules.

**Events** flow via Spring's event publishing (`GenericEventPublisher`/`GenericEventPublisherImpl`). Listeners in `-adapter-listener` modules handle events asynchronously.

**Service-to-service** communication uses `InternalClientTokenProvider` for JWT token caching. Services call `/v1/auth/token-service` when no cached token is available.

### Frontend: Next.js

Located in `frontends/beta-frontend/`. Uses:
- Next.js 15, React 19, TypeScript
- MUI v7 for UI components
- Redux Toolkit + SWR for state/data fetching
- next-auth v4 for authentication
- react-hook-form, i18next, notistack

Config is driven by env vars (see `.env.local.example`). Copy to `.env.local` for local dev.

## Commands

### Backend (from `services/main-service/`)

```bash
# Build and run unit tests
mvn clean install

# Build without tests
mvn clean install -DskipTests=true

# Run only unit tests
mvn test

# Run integration tests (requires Docker for Testcontainers)
mvn clean install -DskipTests=true
mvn failsafe:integration-test failsafe:verify -DskipTestsIT=false -DskipTests=true

# Run a single test class
mvn test -pl {module-name} -Dtest=MyTest

# SonarQube scan (local SonarQube must be running)
mvn verify sonar:sonar \
  -Dsonar.projectKey=main-service \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=<token>
```

Unit tests match `**/*Test.java`; integration tests match `**/*IT.java`.

### Frontend (from `frontends/beta-frontend/`)

```bash
npm run dev          # Start dev server (localhost:3000)
npm run build        # Production build
npm run lint         # ESLint
npm run test         # Jest
npm run test:coverage  # Jest with coverage
```

### Local Environment (from `local-env/`)

```bash
# Start all services (MongoDB + main-service + beta-frontend)
docker compose up -d --build

# Start SonarQube
docker compose -f docker-compose_sonar.yml up -d
```

Services run on:
- Frontend: `http://localhost:3000`
- Main service API: `http://localhost:15001`
- Main service actuator: `http://localhost:15002`
- MongoDB: `mongodb://admin:root@localhost:27017`

## Key Conventions

- **OpenAPI-first**: REST adapters implement generated delegate interfaces from OpenAPI specs. Do not hand-write controllers; implement the delegate.
- **Lombok** is used throughout the backend. Use `@Value`, `@Builder`, `@Data`, `@RequiredArgsConstructor` as appropriate.
- **MapStruct** (`@Mapper`) is used for mapping between domain models and DTOs in REST adapters.
- **Mongock** handles MongoDB migrations.
- **Java 17**, Spring Boot 3.5.0.
- Security uses JWT Bearer tokens. User endpoints require auth; service-to-service uses internal tokens via `/v1/auth/token-service`.
- Lint-staged runs ESLint and Prettier on staged frontend files pre-commit.

## Testing Patterns

- Unit tests use JUnit 5 + Mockito. Test class naming: `*Test.java`.
- Integration tests use Testcontainers for MongoDB. Test class naming: `*IT.java`.
- Frontend uses Jest + React Testing Library. Mocks live in `__mocks__/`.
