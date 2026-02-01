# 4.02-Spring-Boot-Api-Rest-Level2

## 📄 Description

This repository contains a Spring Boot REST API developed as a learning project to practice building a real-world backend application with Java, following clean architecture principles.

The application manages a fruit stock system where each fruit must be associated with a provider. It allows you to:

* Create, update, list and delete providers
* Create, update, list and delete fruits
* Associate fruits with providers
* Filter fruits by provider
* Prevent deleting providers with associated fruits

The project follows a layered architecture:

* Controller → handles HTTP requests and responses
* Service → contains business logic and validations
* Repository → handles data persistence with JPA
* DTOs + Mappers → isolate API layer from domain entities

Global exception handling is implemented using @RestControllerAdvice.

## 💻 Technologies used

- Java 21+
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- JUnit 5
- Spring Boot Test (MockMvc)
- Mockito
- Jackson (JSON serialization)
- Lombok
- Docker & Docker Compose
- IntelliJ IDEA
- Postman

## 📋 Requirements

- Java 21 or higher
- Maven (IntelliJ bundled Maven is sufficient)
- Docker & Docker Compose
- IDE capable of running Spring Boot projects (IntelliJ IDEA, Eclipse, etc.)

## 🛠️ Installation

1. Clone the repository:

```bash
git clone https://github.com/ccasro/4.02-Spring-Boot-Api-Rest-Level2/
```

2. Open the project in your IDE (e.g., IntelliJ IDEA)
3. Ensure Maven dependencies are downloaded automatically

## ▶️ Execution

Using Docker (recommended)

```bash
docker compose up --build
```

The application will start on:

```arduino
http://localhost:8080
```
And MySQL will be available internally via Docker.

## 🌐 API Endpoints

### Providers

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | /providers | Create provider |
| GET    | /providers | List all providers |
| PUT    | /providers/{id} | Update provider |
| DELETE | /providers/{id} | Delete provider |

### Fruits

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | /fruits | Create fruit with provider |
| GET    | /fruits | List all fruits |
| GET    | /fruits?providerId={id} | Filter fruits by provider |
| GET    | /fruits/{id} | Get fruit by id |
| PUT    | /fruits/{id} | Update fruit |
| DELETE | /fruits/{id} | Delete fruit |

## 🧪 Testing

### Unit tests (Mockito)

Service layer is tested in isolation:

* Provider creation logic
* Duplicate name validation
* Provider deletion rules
* Fruit creation with provider validation

### Integration tests (MockMvc)

Controller layer tested end-to-end:

* Create provider
* Conflict on duplicate provider
* Create fruit with provider
* 404 when provider does not exist
* Filter fruits by provider
* Update provider
* Delete provider with/without fruits
* Validation errors (400)
* Not found errors (404)
* Conflict errors (409)

Run all tests:

```bash
mvn test
```

## 🧪 Testing infrastructure (Isolated database)

This project includes a dedicated Docker Compose configuration for automated tests:

```text
docker-compose.test.yml
```

This file spins up an isolated MySQL database used only for testing, ensuring that:
* Development/production data is never affected
* Tests are reproducible
* Tests can run safely in CI/CD pipelines

The test profile is configured in:

```text
application-test.properties
```

To run tests using the isolated database:

```bash
docker compose -f docker-compose.test.yml up -d
mvn test
```


## 🐳 Docker

The project includes:

* Dockerfile for production-ready image
* docker-compose.yml for local infrastructure:
    * MySQL database
    * Spring Boot application

Environment variables control DB configuration:

```env
DB_URL=jdbc:mysql://mysql:3306/fruitdb  
DB_USER=root  
DB_PASSWORD=root
```

## 🤝 Contributions

* Use feature branches for development
* Follow Conventional Commits:
    * feat:
    * fix:
    * refactor:
    * test:
* Keep commits small and focused
* Do not commit secrets or compiled files
* Use Pull Requests for improvements

## 📌 Notes

This project focuses on:

* REST API design
* Layered architecture (Controller → Service → Repository)
* DTO usage and mapping
* Validation with annotations
* JPA relationships (@ManyToOne)
* Business rules enforcement
* Global exception handling
* TDD (Test-Driven Development)
* Dockerized backend services

It represents a complete CRUD API with real production patterns.