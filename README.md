<p align="center">
  <h1 align="center">SmartSpec</h1>
  <p align="center">
    <strong>AI-powered software requirements generator</strong>
  </p>
  <p align="center">
    Transform project descriptions into structured user stories and specifications using Google Gemini AI
  </p>
</p>

<p align="center">
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/Spring%20Boot-3.4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot"></a>
  <a href="https://angular.dev/"><img src="https://img.shields.io/badge/Angular-21-DD0031?style=for-the-badge&logo=angular&logoColor=white" alt="Angular"></a>
  <a href="https://openjdk.org/"><img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"></a>
  <a href="https://www.postgresql.org/"><img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL"></a>
</p>

<p align="center">
  <a href="https://ai.google.dev/"><img src="https://img.shields.io/badge/Google%20Gemini-AI-8E75B2?style=for-the-badge&logo=googlegemini&logoColor=white" alt="Gemini"></a>
  <a href="https://docs.docker.com/compose/"><img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker"></a>
  <a href="https://spring.io/projects/spring-ai"><img src="https://img.shields.io/badge/Spring%20AI-1.0.0-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring AI"></a>
</p>

---

## About The Project

**SmartSpec** is a full-stack web application that leverages **Generative AI** to automate the creation of software requirements. Product managers, developers, and stakeholders can input a project description in natural language, and the system generates structured, well-formatted user stories, acceptance criteria, and technical specifications.

This project demonstrates proficiency in modern enterprise development practices including:

- **AI/LLM Integration** using Spring AI with Google Gemini
- **Clean Architecture** with layered separation of concerns
- **Containerization** with multi-stage Docker builds
- **API-First Development** with OpenAPI/Swagger documentation
- **Database Versioning** with Flyway migrations

### Key Features

- **AI-Powered Generation**: Converts natural language descriptions into structured requirements
- **Project Management**: Create, organize, and manage multiple projects
- **Specification History**: Track and compare generated specifications over time
- **RESTful API**: Well-documented API with Swagger UI
- **Responsive UI**: Modern Angular SPA with reactive patterns

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         FRONTEND                                │
│                    Angular 21 + TypeScript                      │
│              Standalone Components │ Signals │ RxJS             │
└─────────────────────────┬───────────────────────────────────────┘
                          │ HTTP/REST
┌─────────────────────────▼───────────────────────────────────────┐
│                         BACKEND                                 │
│                    Spring Boot 3.4.1                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────────┐  │
│  │ Controllers │──│  Services   │──│      Spring AI          │  │
│  │  REST API   │  │   Logic     │  │  Google Gemini Client   │  │
│  └─────────────┘  └──────┬──────┘  └─────────────────────────┘  │
│                          │                                      │
│  ┌───────────────────────▼──────────────────────────────────┐   │
│  │              Spring Data JPA + Hibernate                 │   │
│  └───────────────────────┬──────────────────────────────────┘   │
└──────────────────────────┼──────────────────────────────────────┘
                           │ JDBC
┌──────────────────────────▼──────────────────────────────────────┐
│                      PostgreSQL 16                              │
│                   Flyway Migrations                             │
└─────────────────────────────────────────────────────────────────┘
```

---

## Tech Stack & Skills Demonstrated

### Backend
| Technology | Purpose | Skills Demonstrated |
|------------|---------|---------------------|
| **Java 21** | Core language | Modern Java features, Records, Pattern Matching |
| **Spring Boot 3.4** | Framework | Auto-configuration, Dependency Injection, Profiles |
| **Spring AI 1.0** | AI Integration | LLM orchestration, Prompt engineering, Streaming |
| **Spring Data JPA** | Data access | Repository pattern, Entity mapping, Transactions |
| **Hibernate** | ORM | Entity relationships, Query optimization |
| **PostgreSQL 16** | Database | Relational modeling, Indexing |
| **Flyway** | Migrations | Version-controlled schema management |
| **Lombok** | Boilerplate reduction | Builder pattern, Logging |
| **MapStruct** | DTO mapping | Type-safe object mapping |
| **SpringDoc OpenAPI** | API documentation | Swagger UI, API-first design |
| **JUnit 5 + Testcontainers** | Testing | Integration testing with real DB |

### Frontend
| Technology | Purpose | Skills Demonstrated |
|------------|---------|---------------------|
| **Angular 21** | Framework | Standalone components, Signals, Zoneless |
| **TypeScript 5.9** | Language | Strong typing, Interfaces, Generics |
| **RxJS** | Reactive programming | Observables, Operators, State management |
| **SCSS** | Styling | Component-scoped styles, Variables |

### DevOps & Infrastructure
| Technology | Purpose | Skills Demonstrated |
|------------|---------|---------------------|
| **Docker** | Containerization | Multi-stage builds, Layer optimization |
| **Docker Compose** | Orchestration | Service dependencies, Health checks, Networks |
| **Maven** | Build tool | Dependency management, Profiles |
| **Git** | Version control | Branching, Conventional commits |

---

## Project Structure

```
SmartSpec/
├── backend/
│   ├── src/main/java/com/smartspec/
│   │   ├── config/          # Configuration classes (CORS, OpenAPI)
│   │   ├── controller/      # REST API endpoints
│   │   ├── service/         # Business logic layer
│   │   ├── repository/      # Data access layer
│   │   ├── model/           # JPA entities
│   │   ├── dto/             # Data Transfer Objects
│   │   └── exception/       # Global exception handling
│   ├── src/main/resources/
│   │   ├── db/migration/    # Flyway SQL migrations
│   │   └── application.yml  # Configuration
│   └── Dockerfile           # Multi-stage production build
├── frontend/
│   ├── src/app/
│   │   ├── components/      # UI components
│   │   ├── services/        # HTTP services
│   │   ├── models/          # TypeScript interfaces
│   │   └── app.routes.ts    # Routing configuration
│   └── Dockerfile           # Development container
├── docs/
│   └── DEVELOPMENT_ROADMAP.md
└── docker-compose.yml       # Full stack orchestration
```

---

## Getting Started

### Prerequisites

- Docker & Docker Compose
- Google AI Studio API Key ([Get one here](https://aistudio.google.com/apikey))

### Installation

```bash
# Clone the repository
git clone https://github.com/NachoOsella/SmartSpec.git
cd SmartSpec

# Create environment file
echo "GOOGLE_API_KEY=your-api-key-here" > .env

# Start all services
docker-compose up --build
```

### Access Points

| Service | URL | Description |
|---------|-----|-------------|
| Frontend | http://localhost:4200 | Angular application |
| Backend API | http://localhost:8080/api | REST endpoints |
| Swagger UI | http://localhost:8080/swagger-ui.html | API documentation |
| Health Check | http://localhost:8080/api/health | Service status |

---

## API Overview

### Projects
```
POST   /api/projects          Create a new project
GET    /api/projects          List all projects
GET    /api/projects/{id}     Get project details
PUT    /api/projects/{id}     Update project
DELETE /api/projects/{id}     Delete project
```

### AI Generation
```
POST   /api/projects/{id}/generate    Generate specifications using AI
GET    /api/projects/{id}/specs       Get generation history
```

---

## Development

### Running Locally

```bash
# Backend only (requires local PostgreSQL)
cd backend
./mvnw spring-boot:run

# Frontend only
cd frontend
npm install
npm start
```

### Useful Commands

```bash
# View logs
docker-compose logs -f backend
docker-compose logs -f frontend

# Rebuild specific service
docker-compose up --build backend

# Database access
docker-compose exec db psql -U postgres -d smartspec_db

# Run tests
cd backend && ./mvnw test
cd frontend && npm test
```

---

## Roadmap

- [x] **Sprint 0**: Infrastructure Setup - Docker, CI/CD, Project structure
- [ ] **Sprint 1**: Data Layer - Entities, Repositories, Migrations
- [ ] **Sprint 2**: AI Integration - Spring AI, Prompt engineering
- [ ] **Sprint 3**: Frontend - Components, Services, State management
- [ ] **Sprint 4**: Production - Security, Optimization, Deployment

---

## Best Practices Implemented

| Category | Practice |
|----------|----------|
| **Security** | Non-root Docker user, Environment-based secrets |
| **Performance** | HikariCP connection pooling, JPA lazy loading |
| **Maintainability** | DTO pattern, Global exception handling |
| **Observability** | Spring Actuator endpoints, Structured logging |
| **API Design** | RESTful conventions, Consistent error responses |
| **Database** | Version-controlled migrations, Validate DDL mode |

---

## Author

**Nacho Osella**

[![GitHub](https://img.shields.io/badge/GitHub-NachoOsella-181717?style=for-the-badge&logo=github)](https://github.com/NachoOsella)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-0A66C2?style=for-the-badge&logo=linkedin)](https://linkedin.com/in/nachoosella)

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  <sub>Built with Spring AI and Google Gemini</sub>
</p>
