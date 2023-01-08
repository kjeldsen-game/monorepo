# Kjeldsen

The Kjeldsen platform has been develop with the idea of micro-services. Each identified business entity will have it's own services and database to perform all actions related with the entity data.

# Tech

## Event-Sourcing
We are using an Event-Sourcing architecture for following key points:
- Data become the numer one level citizen, every single action that occurs in the system gets stored in the form of an event
- The performance of the system will keep a good shape in the long term
- The scalability of the project, both vertically and horizontally will be easy to implement
- The project will be stateless, so the Continuous Deployment, Orchestration, and Containerization are easy to implement
- Events are the final source of truth. Aggregates (views) will be built to always return the most up to date version of an entity.
- Resources:
    - https://www.youtube.com/watch?v=8JKjvY4etTY
    - https://martinfowler.com/eaaDev/EventSourcing.html

## CQRS
The structure of the proyect follows a CQRS pattern https://martinfowler.com/bliki/CQRS.html

## Hexagonal Architecture
The structure of the proyect follows a hexagonal architecture where domain defines the entities and the business logic and adapters connect to any external piece.

## Kafka

The platform will be using Kafka services to produce/consume events.

## Local Development environment

In the folder local-env you can find a project that allows developers to replicate the entire platform quite easy. It is based on Docker Compose and will run up all services and databases needed to work. Read the README of the projects for more information.
