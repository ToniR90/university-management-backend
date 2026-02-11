# Student Management System - Backend

University student management backend system built with Spring Boot 3, following hexagonal architecture principles.

---

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21+** (LTS)
- **Maven 3.8+**
- **Docker & Docker Compose**
- **Git**

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone <your-repository-url>
cd university-management-backend
```

### 2. Environment Setup

Create a `.env` file in the project root:
```env
POSTGRES_DB=students_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
```

### 3. Start PostgreSQL Database

The application uses PostgreSQL as its database. Start it using Docker Compose:
```bash
docker-compose up -d
```

**Verify containers are running:**
```bash
docker ps
```

You should see:
- `students-db` (PostgreSQL 15-alpine) - Status: healthy
- `pgadmin` (optional) - For database management UI

### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

---

## ✅ Verify Setup

### Health Check

Once the application is running, verify it's working correctly:
```bash
curl http://localhost:8080/actuator/health
```

**Expected response:**
```json
{
  "status": "UP"
}
```

### Swagger UI

Interactive API documentation available at: **http://localhost:8080/swagger-ui.html**

All endpoints are auto-documented and explorable via Swagger UI.

### Database Access (pgAdmin)

Optional: Access pgAdmin web interface for database management

- **URL:** http://localhost:5050
- **Email:** admin@admin.com
- **Password:** admin

**Connect to PostgreSQL:**
- Host: `postgres` (container name) or `localhost`
- Port: `5432`
- Database: `students_db`
- Username: `postgres`
- Password: `postgres`

---

## 🌐 REST API

### Base path: `/api/v1/students`

| Method | Path | Description | Status |
|--------|------|-------------|--------|
| POST | `/api/v1/students` | Create student | 201 Created |
| GET | `/api/v1/students` | List all students | 200 OK |
| GET | `/api/v1/students/{id}` | Get student by ID | 200 OK |
| GET | `/api/v1/students/dni/{dni}` | Get student by DNI | 200 OK |
| PATCH | `/api/v1/students/{id}/contact` | Update contact info | 200 OK |
| PATCH | `/api/v1/students/{id}/rgpd` | Update RGPD consent | 200 OK |
| PATCH | `/api/v1/students/{id}/alumni` | Mark as alumni | 200 OK |

### Error Responses

All errors follow a consistent format using `ErrorResponse`:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "No s'ha trobat l'estudiant amb id: 999",
  "timestamp": "2026-02-11T20:00:00"
}
```

| Exception | HTTP Status | When |
|-----------|-------------|------|
| `StudentNotFoundException` | 404 | Student not found by ID or DNI |
| `DuplicateDniException` | 409 | DNI already exists on create |
| `InvalidStudentOperationException` | 400 | Invalid business operation |
| `MethodArgumentNotValidException` | 400 | Request validation fails (@Valid) |
| `IllegalArgumentException` | 400 | Domain validation fails (VOs) |
| `Exception` | 500 | Unexpected errors |

### Postman Collection

A Postman collection is available in the project root for testing all endpoints: `Student-Management-System.postman_collection.json`

Import in Postman: `File → Import → Upload Files`

---

## 🔧 Spring Profiles

The application supports multiple profiles for different environments.

### Available Profiles

| Profile | Purpose | SQL Logging | Health Details | Pool Size |
|---------|---------|-------------|----------------|-----------|
| **dev** | Local development | ✅ Enabled | Always shown | 10 (max) |
| **test** | Automated testing | ❌ Disabled | Never shown | 5 (max) |
| **prod** | Production (future) | ❌ Disabled | When authorized | 20 (max) |

### Running with Profiles

#### Development Profile (Default)
```bash
# Runs with dev profile (default)
mvn spring-boot:run

# Or explicitly
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**Features in dev profile:**
- SQL queries logged and formatted
- Detailed logging (DEBUG level for application code)
- Health endpoint shows all details
- Connection pool: 10 max, 5 min-idle
- Leak detection enabled (30 seconds threshold)

#### Test Profile
```bash
# Run tests (automatically uses test profile)
mvn test

# Or set environment variable
export SPRING_PROFILES_ACTIVE=test
mvn spring-boot:run
```

**Features in test profile:**
- SQL queries NOT logged
- Minimal logging (WARN level)
- Health endpoint details hidden
- Connection pool: 5 max, 2 min-idle
- Leak detection disabled

### Profile Configuration Files
```
src/main/resources/
├── application.yml          # Base configuration (all profiles)
├── application-dev.yml      # Development overrides
└── application-test.yml     # Test overrides
```

---

## 🏗️ Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/orientation/backend/
│   │       ├── StudentManagementSystemApplication.java
│   │       │
│   │       ├── users/
│   │       │   ├── application/
│   │       │   │   ├── commands/
│   │       │   │   │   ├── CreateStudentCommand.java
│   │       │   │   │   ├── UpdateContactCommand.java
│   │       │   │   │   ├── UpdateRgpdConsentCommand.java
│   │       │   │   │   └── MarkAsAlumniCommand.java
│   │       │   │   ├── exceptions/
│   │       │   │   │   ├── StudentNotFoundException.java
│   │       │   │   │   ├── DuplicateDniException.java
│   │       │   │   │   └── InvalidStudentOperationException.java
│   │       │   │   └── services/
│   │       │   │       └── StudentService.java
│   │       │   │
│   │       │   ├── domain/
│   │       │   │   ├── model/
│   │       │   │   │   ├── entities/
│   │       │   │   │   │   └── Student.java              # Aggregate Root
│   │       │   │   │   ├── valueobjects/
│   │       │   │   │   │   ├── Dni.java                  # Spanish DNI/NIE (MOD 23)
│   │       │   │   │   │   ├── Email.java                # Email validation
│   │       │   │   │   │   ├── Phone.java                # E.164 format
│   │       │   │   │   │   ├── FullName.java             # Composite VO
│   │       │   │   │   │   ├── AlumniInfo.java           # Alumni status
│   │       │   │   │   │   └── RgpdConsent.java          # GDPR consent
│   │       │   │   │   └── enums/
│   │       │   │   │       ├── AlumniType.java
│   │       │   │   │       ├── ContactMethod.java
│   │       │   │   │       ├── CurrentYear.java
│   │       │   │   │       ├── DiscoveryChannel.java
│   │       │   │   │       └── RgpdConsentStatus.java
│   │       │   │   └── repository/
│   │       │   │       └── StudentRepository.java         # Domain port (interface)
│   │       │   │
│   │       │   └── infrastructure/
│   │       │       ├── persistence/
│   │       │       │   ├── entities/
│   │       │       │   │   └── StudentJpaEntity.java
│   │       │       │   ├── mappers/
│   │       │       │   │   └── StudentJpaMapper.java
│   │       │       │   └── repositories/
│   │       │       │       ├── SpringDataStudentRepository.java
│   │       │       │       └── StudentRepositoryImpl.java
│   │       │       └── web/
│   │       │           ├── controller/
│   │       │           │   └── StudentController.java
│   │       │           ├── exception/
│   │       │           │   └── RestExceptionHandler.java
│   │       │           └── dto/
│   │       │               ├── request/
│   │       │               │   ├── CreateStudentRequest.java
│   │       │               │   ├── UpdateContactRequest.java
│   │       │               │   ├── UpdateRgpdRequest.java
│   │       │               │   └── MarkAlumniRequest.java
│   │       │               └── response/
│   │       │                   ├── StudentResponse.java
│   │       │                   └── ErrorResponse.java
│   │       │
│   │       ├── sessions/       # Session management (Sprint 3)
│   │       ├── auth/           # Authentication (Sprint 4)
│   │       └── shared/
│   │           └── infrastructure/
│   │               └── config/
│   │                   ├── ApplicationConfig.java
│   │                   ├── CorsConfig.java
│   │                   └── DatabaseHealthIndicator.java
│   │
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-test.yml
│       └── db/
│           └── migration/
│               ├── V1__create_students_table.sql
│               └── V2__change_integer_types.sql
│
└── test/
    └── java/
        └── com/orientation/backend/
            ├── ApplicationContextTest.java
            └── users/
                ├── application/
                │   └── services/
                │       └── StudentServiceTest.java
                ├── domain/
                │   └── model/
                │       ├── entities/
                │       │   └── StudentTest.java
                │       └── valueobjects/
                │           ├── AlumniInfoTest.java
                │           ├── DniTest.java
                │           ├── EmailTest.java
                │           ├── FullNameTest.java
                │           ├── PhoneTest.java
                │           └── RgpdConsentTest.java
                └── infrastructure/
                    ├── persistence/
                    │   └── repositories/
                    │       └── StudentRepositoryImplTest.java
                    └── web/
                        └── controller/
                            └── StudentControllerTest.java
```

---

## 📚 Architecture

This project follows **Hexagonal Architecture** (Ports & Adapters) and **Domain-Driven Design** principles:

```
┌─────────────────────────────────────────────────┐
│              INFRASTRUCTURE                      │
│    REST API, Database, Configuration             │
│                                                  │
│    ┌───────────────────────────────────────┐     │
│    │          APPLICATION                  │     │
│    │   Services, Commands, Exceptions      │     │
│    │                                       │     │
│    │    ┌───────────────────────────┐      │     │
│    │    │         DOMAIN            │      │     │
│    │    │  Entities, VOs, Enums     │      │     │
│    │    └───────────────────────────┘      │     │
│    └───────────────────────────────────────┘     │
└─────────────────────────────────────────────────┘
```

**Dependency rule:** Dependencies always point inward. The domain knows nothing about the outside world.

### Domain Layer (Core)
Pure business logic with no framework dependencies. Contains the Student aggregate root, 6 value objects with self-validation, 5 enums, and the repository interface (port).

### Application Layer (Orchestration)
Coordinates operations between the outside world and the domain. Contains the StudentService, command objects (CreateStudentCommand, UpdateContactCommand, etc.), and application-specific exceptions.

### Infrastructure Layer (Technical Details)
Implements the technical concerns: REST controllers with global exception handling, JPA persistence with bidirectional mapping (Domain ↔ JPA), Spring Data repositories, REST DTOs, Swagger UI, CORS configuration, and database health monitoring.

### Request Flow

```
HTTP Request
    → StudentController (validate + delegate)
        → StudentService (orchestrate)
            → Domain (business logic)
                → StudentRepository port
                    → StudentRepositoryImpl adapter
                        → Spring Data JPA → PostgreSQL
    ← StudentResponse (via fromDomain)
← HTTP Response
```

### Repository Pattern (Ports & Adapters)

```
Domain                         Infrastructure
┌──────────────────┐           ┌──────────────────────────┐
│ StudentRepository│◄──────────│ StudentRepositoryImpl    │
│   (port)         │           │   (adapter)              │
└──────────────────┘           │                          │
                               │  SpringDataStudentRepo   │
                               │  StudentJpaEntity        │
                               │  StudentJpaMapper        │
                               └──────────────────────────┘
```

---

## 🎯 Domain Model

### Student Entity (Aggregate Root)

The `Student` entity is the aggregate root with the following structure:

**Immutable fields:** DNI, full name, current year, creation timestamp
**Mutable fields:** Email, phone, alumni info, RGPD consent, discovery/contact channels, notes

**Business operations:**
- Email/Phone management (add, update, remove, atomic update)
- RGPD consent tracking (3 signature methods + pending)
- Alumni status management (mark/unmark with type and year)
- Discovery and contact channel registration
- Counselor notes management

Built using the **Builder pattern** with intelligent defaults: new students are automatically created as non-alumni with pending RGPD consent.

---

### Value Objects

| Value Object | Purpose | Key Validation |
|---|---|---|
| **Dni** | Spanish DNI/NIE | MOD 23 algorithm, letter validation, DNI and NIE support |
| **Email** | Email address | Format validation, lowercase normalization |
| **Phone** | Phone number | E.164 international format, auto +34 prefix for Spanish numbers |
| **FullName** | Person's name | Name + first surname required, second surname optional |
| **AlumniInfo** | Alumni status | Composite: if alumni → type + year required; if not → both null |
| **RgpdConsent** | GDPR consent | Composite: 4 statuses with different required fields per status |

All value objects are **immutable**, **self-validated** (if it exists, it's valid), and created via **factory methods**.

**Examples:**
```java
Dni.of("12345678Z")                              // Valid DNI
Dni.of("X1234567L")                              // Valid NIE
Email.of("STUDENT@University.edu")               // Normalized to lowercase
Phone.of("600123456")                            // Auto-prefixed to +34600123456
FullName.of("Joan", "García", null)              // Optional second surname
AlumniInfo.notAlumni()                           // Non-alumni (no type/year allowed)
AlumniInfo.createAlumni(AlumniType.MASTER, 2023) // Alumni with required fields
RgpdConsent.pending()                            // Awaiting consent
RgpdConsent.signedInPerson()                     // Signed now (auto-timestamp)
RgpdConsent.alreadySigned(2020)                  // Previously signed (year required)
```

---

### Enums

#### AlumniType
`BACHELOR`, `MASTER`, `DOCTORATE`, `DOUBLE_DEGREE`, `ERASMUS`, `EXCHANGE`, `OTHER`

#### ContactMethod
`EMAIL`, `PHONE`, `IN_PERSON`, `ONLINE_FORM`, `REFERRAL`, `OTHER`

#### CurrentYear
`FIRST`, `SECOND`, `THIRD`, `FOURTH`, `FIFTH`, `SIXTH`, `MASTER`, `DOCTORATE`

Includes domain logic: `isGraduateLevel()`, `isUndergraduate()`

#### DiscoveryChannel
`WEBSITE`, `SOCIAL_MEDIA`, `REFERRAL`, `UNIVERSITY_EVENT`, `EMAIL_CAMPAIGN`, `OTHER`

#### RgpdConsentStatus
`PENDING`, `SIGNED_IN_PERSON`, `SIGNED_ONLINE`, `ALREADY_SIGNED`

Includes domain logic: `isPending()`, `isSigned()`, `requiresYear()`, `requiresDate()`

---

## 🧪 Testing

### Test Summary

| Layer | Test Class | Tests | Type |
|---|---|---|---|
| Domain - VOs | DniTest | 23 | Unit |
| Domain - VOs | FullNameTest | 14 | Unit |
| Domain - VOs | PhoneTest | 12 | Unit |
| Domain - VOs | AlumniInfoTest | 11 | Unit |
| Domain - VOs | RgpdConsentTest | 10 | Unit |
| Domain - VOs | EmailTest | 9 | Unit |
| Domain - Entity | StudentTest | 31 | Unit |
| Application | StudentServiceTest | 13 | Unit (Mockito) |
| Infrastructure | StudentRepositoryImplTest | 11 | Integration (PostgreSQL) |
| Infrastructure | StudentControllerTest | 14 | Web (MockMvc) |

**Total: ~148 tests** across all layers.

### Test Approach by Layer

**Domain tests (unit):** No mocks, no Spring context. Pure Java testing of business rules, validations, and value object behavior.

**Application tests (unit + Mockito):** Repository mocked with `@Mock`. Tests verify service orchestration, command handling, and exception flow without touching the database.

**Infrastructure - persistence (integration):** `@DataJpaTest` with real PostgreSQL. Tests verify the full persistence cycle: Domain → Mapper → JPA → DB → JPA → Mapper → Domain.

**Infrastructure - web (MockMvc):** `@WebMvcTest` with mocked services. Tests verify HTTP status codes, request validation, error response format, and JSON structure without starting the full application.

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=StudentTest

# Run specific layer
mvn test -Dtest="com.orientation.backend.users.domain.**"

# Run only controller tests
mvn test -Dtest=StudentControllerTest
```

**Note:** Integration tests require PostgreSQL running (via Docker).

---

## 🐳 Docker Commands

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs postgres

# Remove volumes (clean database)
docker-compose down -v
```

---

## 🔧 Troubleshooting

### Port 5432 Already in Use
```bash
# Check what's using the port
lsof -i :5432          # macOS/Linux
netstat -ano | findstr :5432  # Windows
```
Change port in `docker-compose.yml` to `5433:5432` and update `DB_URL` accordingly.

### Application Fails to Start
1. Verify Docker is running: `docker ps`
2. Check PostgreSQL health: `docker-compose logs postgres`
3. Verify Java version: `java -version` (must be 21+)
4. Clean rebuild: `mvn clean install`

### Flyway Migration Fails
1. Verify database exists via pgAdmin
2. Check migration files in `src/main/resources/db/migration/`
3. Ensure no manual schema changes conflict with migrations

---

## 🗺️ Roadmap

### Completed — Sprint 1 ✅

- [x] **Task #7:** Student Domain Model
  - Database migration (Flyway V1 + V2)
  - Domain enums (5 types with display names and business logic)
  - Value Objects (6 VOs with self-validation)
  - Student Entity (Aggregate Root with Builder pattern)
  - Unit tests (110 tests)

- [x] **Task #8:** Student Repository Layer
  - JPA Entity mapping (StudentJpaEntity)
  - Bidirectional mapper (Domain ↔ JPA)
  - Repository pattern (Port + Adapter + Spring Data)
  - Integration tests (11 tests with PostgreSQL)

- [x] **Task #9:** Student Application Layer
  - StudentService with CRUD orchestration
  - Application commands (Create, UpdateContact, UpdateRgpd, MarkAsAlumni)
  - Application exceptions (StudentNotFound, DuplicateDni, InvalidOperation)
  - Service tests with Mockito (13 tests)
  - REST DTOs (requests + responses)

- [x] **Task #10:** REST API Controllers
  - StudentController with 7 endpoints
  - Global exception handler (@RestControllerAdvice)
  - Request validation with @Valid
  - Swagger UI auto-generated documentation
  - Controller tests with MockMvc (14 tests)
  - Postman collection for manual testing

### Upcoming
- [ ] **Sprint 2:** Refactoring + DELETE endpoint + additional operations
- [ ] **Sprint 3:** Sessions Module (Collaborators + Sessions)
- [ ] **Sprint 4:** Authentication & Authorization (JWT)

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 (LTS) |
| Framework | Spring Boot 3.3.3 |
| Build | Maven 3.9 |
| Database | PostgreSQL 15 |
| Migrations | Flyway |
| ORM | Spring Data JPA / Hibernate |
| API Docs | springdoc-openapi (Swagger UI) |
| Testing | JUnit 5 + Mockito + AssertJ + MockMvc |
| Containers | Docker + Docker Compose |
| Monitoring | Spring Boot Actuator |
| Dev Tools | Lombok, Spring Boot DevTools, Postman |

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Toni Romero** — [@tonir90](https://github.com/tonir90)

---

## 📅 Recent Updates

### February 2026

**Sprint 1 Completed** 🎉

**Task #10: REST API Controllers** ✅ *Completed*
- Implemented StudentController with 7 REST endpoints
- Built global exception handler with consistent error responses
- Added Swagger UI for interactive API documentation
- Created Postman collection for manual testing
- 14 controller tests with MockMvc

**Task #9: Application Layer** ✅ *Completed*
- Implemented StudentService with full CRUD orchestration
- Created command objects for all write operations
- Built application-specific exceptions with meaningful messages
- Added REST DTOs (4 requests + 2 responses)
- 13 service tests with Mockito

**Task #8: Repository Layer** ✅ *Completed*
- Implemented hexagonal architecture with Ports & Adapters pattern
- Created JPA entity mapping with bidirectional mapper
- 11 integration tests with real PostgreSQL

**Task #7: Domain Model** ✅ *Completed*
- Complete domain model with 6 value objects and Student aggregate root
- 110 unit tests covering all domain logic

**Last Updated:** February 2026