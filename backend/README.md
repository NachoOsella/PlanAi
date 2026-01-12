# PlanAI Backend

Backend API for PlanAI, a conversational AI application for project planning.

## Technology Stack

- **Framework**: Spring Boot 3.4.1
- **Language**: Java 21
- **Database**: PostgreSQL 16
- **AI Integration**: Google Gemini SDK
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)

## Project Structure

```
src/main/java/com/planai/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── exception/        # Exception handling
├── mapper/           # DTO-Entity mappers
├── model/
│   ├── dto/          # Data Transfer Objects
│   ├── entity/       # JPA Entities
│   └── enums/        # Enumerations
├── repository/       # Spring Data JPA Repositories
└── service/          # Business logic
```

## API Documentation

Interactive API documentation is available via Swagger UI:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080:8080/swagger-ui.html)
- **OpenAPI Docs**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Getting Started

### Prerequisites

- Java 21
- PostgreSQL 16
- Docker (optional)

### Running with Docker

```bash
docker-compose up --build
```

### Running Locally

```bash
# Build
./mvnw clean install

# Run
./mvnw spring-boot:run
```

### Running Tests

```bash
./mvnw test
```

## Configuration

Configuration is managed via `application.yml`:

- Database connection: PostgreSQL on localhost:5432/planai_db
- CORS: Enabled for localhost:4200 (Angular frontend)
- AI: Google Gemini (gemini-3-flash-preview)

## Error Handling

The API uses a consistent error response format:

```json
{
    "timestamp": "2024-01-01T12:00:00",
    "status": 400,
    "error": "VALIDATION_ERROR",
    "message": "Validation failed",
    "path": "/api/projects",
    "fieldErrors": [
        {
            "field": "name",
            "message": "must not be blank"
        }
    ]
}
```

## License

MIT
