# AGENTS.md - Project Documentation for AI Agents

## Project Overview

**grammr** is a language learning platform that provides grammatical analysis, translation,
word inflection, and flashcard creation capabilities across multiple languages.
The system consists of a Java Spring Boot backend, a Next.js frontend,
and several Python-based microservices.

## Architecture

### Core Components

- **Backend**: Java 21 Spring Boot application (`grammr-core`)
- **Frontend**: Next.js 15 with TypeScript, Tailwind CSS, and Radix UI (`grammr-ui/`)
- **Database**: PostgreSQL with Flyway migrations
- **Authentication**: Clerk.js for user authentication
- **Microservices**: Python-based services for specialized language processing

### Directory Structure

```
├── src/                          # Backend Java source code
│   ├── main/java/com/grammr/     # Main application code
│   │   ├── Grammr.java           # Spring Boot main class
│   │   ├── domain/               # Domain entities and models
│   │   ├── flashcards/           # Flashcard management
│   │   ├── chat/                 # Chat/conversation features
│   │   ├── language/             # Language-specific services
│   │   ├── config/               # Configuration classes
│   │   └── repository/           # Data access layer
│   └── main/resources/           # Configuration and migration files
│       ├── db/migration/         # Flyway database migrations
│       └── application.yml       # Main configuration
├── grammr-ui/                    # Next.js frontend application
│   └── src/                      # Frontend source code
│       ├── app/                  # Next.js app router pages
│       ├── components/           # Reusable React components
│       ├── context/              # React context providers
│       ├── hooks/                # Custom React hooks
│       ├── lib/                  # Utility functions
│       └── types/                # TypeScript type definitions
├── sidecars/                     # Python microservices
│   ├── anki-exporter/            # Anki flashcard export service
│   ├── morphology/               # Grammatical analysis service
│   ├── inflection-ru/            # Russian inflection service
│   ├── inflection-de/            # German inflection service
│   └── multi-inflection/         # Multi-language inflection service
├── local/                        # Local development environment
│   ├── docker-compose.yaml       # Development services
│   └── data/                     # Database setup scripts
└── Taskfile.yaml                 # Task automation and lifecycle management
```

## Development Lifecycle

### Task Management

The project uses [Task](https://taskfile.dev/) for lifecycle management. Key commands:

```bash
# Backend testing and validation
task test                  # Run full test suite (unit + integration)
task unit-test            # Run unit tests only
task integration-test     # Run integration tests with environment

# Frontend testing and validation
task lint                 # Run frontend linter, type checking, and build validation

# Environment management
task start-env            # Start PostgreSQL, mockserver, and microservices
task stop-env             # Stop local development environment
task run                  # Start full application (backend + frontend)

# Individual components
task start-frontend       # Start Next.js dev server only
```

### Testing Strategy

- **Unit Tests**: Maven Surefire (`./mvnw test`)
- **Integration Tests**: Requires Docker environment with PostgreSQL and microservices
- **Frontend**: ESLint, TypeScript checking, and build validation via `task lint`

## Core Domain Model

### Entities

Based on the database schema and domain classes:

- **User**: User accounts with language preferences (spoken/learned)
- **Deck**: Collections of flashcards organized by topic/language
- **Flashcard**: Individual learning cards with front/back content, paradigm references
- **Chat/ChatMessage**: Conversation system for language learning interactions
- **Analysis**: Grammatical analysis results from morphology services
- **Paradigm**: Language-specific grammatical patterns and inflections

### API Structure

The backend follows REST conventions with versioned APIs:
- Base path: `/api/v2/`
- Example: `/api/v2/deck/{deckId}/flashcard` (from FlashcardController)

## Microservices Architecture

### Service Communication

All microservices are Python-based and communicate via HTTP APIs:

1. **morphology** (ports 8010-8011): Grammatical analysis using spaCy
   - Supports Russian (`ru_core_news_sm`) and German (`de_core_news_sm`) models
   - Health endpoint: `/health`

2. **inflection-ru** (port 8020): Russian word inflection
3. **anki-exporter**: Converts deck entities to Anki-compatible formats (APKG, CSV, DB)
4. **multi-inflection**: Multi-language inflection service

### External Dependencies

- **OpenAI API**: For AI-powered language features
- **Clerk**: Authentication and user management
- **PostgreSQL**: Primary database with `grammr` schema

## Development Environment Setup

### Prerequisites

- Java 21
- Node.js/pnpm (for frontend)
- Docker and Docker Compose
- Task runner
- PostgreSQL (via Docker)

### Quick Start

```bash
# 1. Start supporting services
task start-env

# 2. Run backend tests
task test

# 3. Start full application
task run
```

## Configuration

### Environment Variables

Key environment variables (see `application.yml`):
- `DATASOURCE_URL`: PostgreSQL connection string
- `OPENAI_API_KEY`: OpenAI API access
- `JWT_PUBLIC_KEY`: Clerk JWT verification key
- `ALLOWED_ORIGIN`: CORS configuration
- `ANKI_EXPORTER_URL`: Anki export service URL

### Profiles

- `local`: Development profile with local database and mock services
- Default: Production configuration

## Key Technologies

### Backend Stack
- **Framework**: Spring Boot 3.5.0
- **Java Version**: 21
- **Database**: PostgreSQL with Flyway migrations
- **Authentication**: JWT via Clerk
- **Documentation**: OpenAPI/Swagger
- **Testing**: JUnit, Spring Boot Test
- **Build**: Maven

### Frontend Stack
- **Framework**: Next.js 15 with App Router
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **UI Components**: Radix UI + shadcn/ui
- **State Management**: React Context
- **Authentication**: Clerk.js
- **Package Manager**: pnpm

### Microservices
- **Language**: Python
- **Framework**: FastAPI (inferred from health endpoints)
- **NLP**: spaCy for morphological analysis
- **Containerization**: Docker

## Common Patterns

### Code Organization
- Controllers use versioned packages (`controller.v2`)
- DTOs are colocated with controllers
- Services are separated by domain (flashcards, language, etc.)
- Repository pattern for data access

### Error Handling
- Custom exceptions in `domain.exception` package
- Standard HTTP status codes
- Comprehensive API documentation via OpenAPI

### Security
- JWT-based authentication via Clerk
- CORS configuration for frontend integration
- User-scoped resource access patterns

## Troubleshooting

### Common Issues
1. **Integration tests failing**: Ensure `task start-env` has completed successfully
2. **Frontend build errors**: Run `task lint` to identify TypeScript/ESLint issues
3. **Database migration issues**: Check Flyway migrations in `src/main/resources/db/migration/`
4. **Microservice connectivity**: Verify Docker containers are healthy via health endpoints

### Debugging
- Backend logs: Standard Spring Boot logging
- Frontend: Next.js development server logs
- Database: Direct PostgreSQL access via connection string
- Microservices: Docker container logs and health endpoints
