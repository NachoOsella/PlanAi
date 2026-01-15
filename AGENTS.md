# PlanAI Agent Guidelines

This document provides essential instructions, commands, and conventions for AI agents working on the PlanAI repository.

## 1. Project Overview

**PlanAI** is a conversational AI application for project planning.
- **Frontend**: Angular 21 + TypeScript 5.9 + SCSS
- **Backend**: Spring Boot 3.3.4 + Java 21 + PostgreSQL 16
- **AI**: Spring AI with Groq (Llama 3.3)

## 2. Build, Run, and Test Commands

### Backend (`/backend`)
*Working Directory: `/backend`*

- **Build**: `./mvnw clean install`
- **Run Application**: `./mvnw spring-boot:run`
- **Run All Tests**: `./mvnw test`
- **Run Single Test Class**: `./mvnw -Dtest=TestClassName test`
- **Run Single Test Method**: `./mvnw -Dtest=TestClassName#methodName test`
- **Database**: PostgreSQL (ensure it is running via Docker)

### Frontend (`/frontend`)
*Working Directory: `/frontend`*

- **Install Dependencies**: `npm install`
- **Start Development Server**: `npm start` (Runs at `http://localhost:4200`)
- **Build for Production**: `npm run build`
- **Run Tests**: `npm test` (Uses Vitest)
- **Lint**: `ng lint` (if configured)

### Infrastructure
*Working Directory: Project Root*

- **Start All Services**: `docker-compose up --build`
- **Stop All Services**: `docker-compose down`

## 3. Code Style & Conventions

### General
- **Indentation**: 2 spaces (Frontend), 4 spaces (Backend/Java standard)
- **Files**: Ensure all new files end with a newline.

### Backend (Java/Spring Boot)
- **Java Version**: 21
- **Framework**: Spring Boot 3.3.4
- **AI Integration**: Spring AI with Groq (Llama 3.3)
- **Context Strategy**: Inject full project hierarchy (Epics/Stories/Tasks) into System Prompt.
- **Boilerplate**: Use **Lombok** (`@Data`, `@Builder`, `@RequiredArgsConstructor`, `@Slf4j`) to reduce boilerplate.
- **DTO Mapping**: Use **ModelMapper** or MapStruct.
- **API Documentation**: Use **SpringDoc OpenAPI** annotations for controllers.
- **Database**: Use Spring Data JPA repositories.
- **Validation**: Use Jakarta Validation (`@Valid`, `@NotNull`, etc.) in DTOs.
- **Naming**:
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - DTOs: suffix with `Request`, `Response`, or `Dto`.
  - Entities: suffix with `Entity` (e.g., `ProjectEntity`).

### Frontend (Angular)
- **Framework**: Angular 21 (Latest)
- **Architecture**:
  - Use **Standalone Components** (`standalone: true`).
  - Use **Signals** for state management and reactivity.
  - Use **RxJS** for complex async streams and HTTP calls.
- **Formatting** (Prettier rules from `package.json`):
  - `printWidth`: 100
  - `singleQuote`: true
- **Structure**:
  - `core/`: Singleton services, models, interceptors.
  - `features/`: Feature modules (e.g., `chat`, `projects`).
  - `shared/`: Reusable components (buttons, cards).
- **Naming**:
  - Files: `kebab-case` (e.g., `project-list.component.ts`).
  - Classes: `PascalCase` (e.g., `ProjectListComponent`).
  - Selectors: `app-kebab-case` (e.g., `app-project-list`).

## 4. Testing Guidelines

- **Backend**:
  - Write **JUnit 5** tests.
  - Use `@SpringBootTest` for integration tests.
  - Use `@WebMvcTest` for controller slice tests.
  - Mock dependencies using **Mockito** (`@MockBean`).
- **Frontend**:
  - Write **Vitest** tests.
  - Test components using `ComponentFixture`.
  - Mock services to isolate component logic.

## 5. Documentation

- Update `README.md` if architectural changes are made.
- Add Javadoc for complex business logic in the backend.
- Ensure API endpoints are documented with Swagger annotations.

## 6. Git & Version Control

- **Commit Messages**:
  - Format: `<type>: <subject>`
  - Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`.
  - Example: `feat: add project creation endpoint`

## 7. Configuration

- **Environment Variables**: Managed via `.env` file (see `README.md`).
- **Backend Config**: `src/main/resources/application.yml`
- **Docker**: `docker-compose.yml` and `Dockerfile` in respective directories.
