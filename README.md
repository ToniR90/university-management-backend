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

Interactive API documentation available at: **http://localhost:8080/swagger-ui/index.html**

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
| GET | `/api/v1/students` | Search students (paginated + filtered) | 200 OK |
| GET | `/api/v1/students/{id}` | Get student by ID | 200 OK |
| GET | `/api/v1/students/dni/{dni}` | Get student by DNI | 200 OK |
| PATCH | `/api/v1/students/{id}/contact` | Update contact info | 200 OK |
| PATCH | `/api/v1/students/{id}/rgpd` | Update RGPD consent | 200 OK |
| PATCH | `/api/v1/students/{id}/alumni` | Mark as alumni | 200 OK |
| DELETE | `/api/v1/students/{id}` | Soft delete student | 204 No Content |

### Pagination & Filtering

The `GET /api/v1/students` endpoint supports pagination and composable filters:

**Query Parameters:**

| Param | Type | Default | Description |
|-------|------|---------|-------------|
| `page` | int | 0 | Page number (>= 0) |
| `size` | int | 20 | Elements per page (1-100) |
| `name` | String | — | Partial match, case-insensitive |
| `dni` | String | — | Exact match |
| `currentYear` | String | — | Enum value (FIRST, SECOND...) |
| `degree` | String | — | Enum value (NURSING, COMPUTER_ENGINEERING...) |
| `isAlumni` | Boolean | — | true / false |

All filters are optional and combinable. Only active students are returned (soft-deleted excluded).

**Example requests:**
```
GET /api/v1/students                                    → All students, page 0, size 20
GET /api/v1/students?page=1&size=5                      → Page 1, 5 per page
GET /api/v1/students?name=Joan                          → Students named "Joan" (case-insensitive)
GET /api/v1/students?degree=NURSING                     → Nursing students only
GET /api/v1/students?currentYear=FIRST&isAlumni=false   → First year, non-alumni
GET /api/v1/students?degree=COMPUTER_ENGINEERING&currentYear=FIRST&page=0&size=10  → Combined
```

**Response format:**
```json
{
  "content": [
    {
      "id": 1,
      "dni": "12345678Z",
      "name": "Joan",
      "firstSurname": "García",
      "degree": "NURSING",
      "degreeName": "Grau en Infermeria",
      ...
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 87,
  "totalPages": 5
}
```

### Degree (Available Values)

The `degree` field is a controlled enum with 22 university degrees:

| Enum Value | Display Name |
|---|---|
| `ELECTRONIC_ENGINEERING` | Grau en Enginyeria Electrònica Industrial i Automàtica |
| `COMPUTER_ENGINEERING` | Grau en Enginyeria Informàtica de Gestió i Sistemes d'Informació |
| `MECHANICAL_ENGINEERING` | Grau en Enginyeria Mecànica |
| `INDUSTRIAL_ORGANIZATION` | Grau en Enginyeria d'Organització Industrial |
| `AI_AND_ROBOTICS` | Grau en Intel·ligència Artificial i Robòtica Aplicada |
| `DOUBLE_CS_VIDEOGAMES` | Doble titulació en Enginyeria Informàtica i Disseny i Producció de Videojocs |
| `AUDIOVISUAL_MEDIA` | Grau en Mitjans Audiovisuals |
| `VIDEOGAME_DESIGN` | Grau en Disseny i Producció de Videojocs |
| `DOUBLE_ELECTRONIC_MECHANICAL` | Simultaneïtat d'Enginyeria Electrònica i Mecànica |
| `DOUBLE_ELECTRONIC_CS` | Simultaneïtat d'Enginyeria Electrònica i Informàtica |
| `DOUBLE_VIDEOGAMES_AUDIOVISUAL` | Simultaneïtat de Disseny i Producció de Videojocs + Mitjans Audiovisuals |
| `BUSINESS_ADMINISTRATION` | Grau en Administració d'Empreses i Gestió de la Innovació |
| `BUSINESS_ADMINISTRATION_EN` | Grau en Administració d'Empreses i Gestió de la Innovació (docència en anglès) |
| `DIGITAL_MARKETING` | Grau en Màrqueting i Comunitats Digitals |
| `MARITIME_LOGISTICS` | Grau en Logística i Negocis Marítims |
| `DOUBLE_TOURISM_BUSINESS` | Doble titulació en Turisme i Gestió de l'Oci i ADE i Gestió de la Innovació |
| `DOUBLE_BUSINESS_MARKETING` | Doble titulació en ADE i Gestió de la Innovació i Màrqueting i Comunitats Digitals |
| `NURSING` | Grau en Infermeria |
| `SPORTS_SCIENCE` | Grau en Ciències de l'Activitat Física i de l'Esport (CAFE) |
| `PHYSIOTHERAPY` | Grau en Fisioteràpia |
| `DOUBLE_PHYSIO_SPORTS` | Doble titulació en Fisioteràpia i Ciències de l'Activitat Física i de l'Esport (CAFE) |
| `DOUBLE_TOURISM_MARKETING` | Doble titulació en Turisme i Màrqueting |
| `NUTRITION` | Grau en Nutrició Humana i Dietètica |

The API response includes both `degree` (enum value for programmatic use) and `degreeName` (display name for UI).

### Soft Delete

`DELETE /api/v1/students/{id}` performs a soft delete: sets `active = false` and records `deletedAt` timestamp. The student is excluded from all queries but remains in the database. A partial unique index allows DNI reuse after deactivation.

### Error Responses

All errors follow a consistent format using `ApiError`:

```json
{
  "timestamp": "2026-03-06T14:00:00",
  "status": 404,
  "errorCode": "USER_NOT_FOUND",
  "message": "Multiple business rule violations",
  "errors": [
    {
      "field": "id",
      "message": "User not found with id 999"
    }
  ]
}
```

Supports multiple validation errors in a single response.

| Exception | HTTP Status | When |
|-----------|-------------|------|
| `StudentNotFoundException` | 404 | Student not found by ID or DNI |
| `CreatedStudentException` | 409 | DNI already exists on create |
| `UpdateStudentException` | 400 | Invalid business operation |
| `MethodArgumentNotValidException` | 400 | Request validation fails (@Valid) |
| `MethodArgumentTypeMismatchException` | 400 | Invalid enum or type in query params |
| `IllegalArgumentException` | 400 | Domain validation fails (VOs, Pagination, Degree) |
| `Exception` | 500 | Unexpected errors |

### Postman Collection

A Postman collection is available in the project root for testing all endpoints: `Student_Management_System_Sprint_2_Pagination.postman_collection.json`

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
mvn spring-boot:run
```

#### Test Profile
```bash
mvn test
```

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
│   │       │   │   │   ├── core/
│   │       │   │   │   │   ├── BusinessValidationException.java
│   │       │   │   │   │   ├── BusinessViolation.java
│   │       │   │   │   │   └── ErrorCode.java
│   │       │   │   │   ├── CreatedStudentException.java
│   │       │   │   │   ├── StudentNotFoundException.java
│   │       │   │   │   └── UpdateStudentException.java
│   │       │   │   └── services/
│   │       │   │       └── StudentService.java
│   │       │   │
│   │       │   ├── domain/
│   │       │   │   ├── model/
│   │       │   │   │   ├── entities/
│   │       │   │   │   │   └── Student.java
│   │       │   │   │   ├── valueobjects/
│   │       │   │   │   │   ├── Dni.java
│   │       │   │   │   │   ├── Email.java
│   │       │   │   │   │   ├── Phone.java
│   │       │   │   │   │   ├── FullName.java
│   │       │   │   │   │   ├── AlumniInfo.java
│   │       │   │   │   │   └── RgpdConsent.java
│   │       │   │   │   ├── enums/
│   │       │   │   │   │   ├── AlumniType.java
│   │       │   │   │   │   ├── ContactMethod.java
│   │       │   │   │   │   ├── CurrentYear.java
│   │       │   │   │   │   ├── Degree.java
│   │       │   │   │   │   ├── DiscoveryChannel.java
│   │       │   │   │   │   └── RgpdConsentStatus.java
│   │       │   │   │   └── query/
│   │       │   │   │       ├── Pagination.java
│   │       │   │   │       ├── PageResult.java
│   │       │   │   │       └── StudentSearchCriteria.java
│   │       │   │   ├── exceptions/
│   │       │   │   │   └── StudentAlreadyInactiveException.java
│   │       │   │   └── repository/
│   │       │   │       └── StudentRepository.java
│   │       │   │
│   │       │   └── infrastructure/
│   │       │       ├── persistence/
│   │       │       │   ├── entities/
│   │       │       │   │   └── StudentJpaEntity.java
│   │       │       │   ├── mappers/
│   │       │       │   │   └── StudentJpaMapper.java
│   │       │       │   ├── repositories/
│   │       │       │   │   ├── SpringDataStudentRepository.java
│   │       │       │   │   └── StudentRepositoryImpl.java
│   │       │       │   └── specifications/
│   │       │       │       └── StudentSpecifications.java
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
│   │       │                   ├── PagedStudentResponse.java
│   │       │                   ├── ApiError.java
│   │       │                   ├── FieldErrorDetail.java
│   │       │                   └── ErrorResponse.java
│   │       │
│   │       ├── sessions/
│   │       ├── auth/
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
│               ├── V2__change_integer_types.sql
│               └── V3__add_soft_delete.sql
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
                │       ├── valueobjects/
                │       │   ├── AlumniInfoTest.java
                │       │   ├── DniTest.java
                │       │   ├── EmailTest.java
                │       │   ├── FullNameTest.java
                │       │   ├── PhoneTest.java
                │       │   └── RgpdConsentTest.java
                │       └── query/
                │           ├── PaginationTest.java
                │           ├── PageResultTest.java
                │           └── StudentSearchCriteriaTest.java
                └── infrastructure/
                    ├── persistence/
                    │   └── repositories/
                    │       └── StudentRepositoryImplTest.java
                    └── web/
                        ├── controller/
                        │   └── StudentControllerTest.java
                        └── dto/
                            ├── request/
                            │   ├── CreateStudentRequestTest.java
                            │   ├── MarkAlumniRequestTest.java
                            │   ├── UpdateContactRequestTest.java
                            │   └── UpdateRgpdRequestTest.java
                            └── response/
                                ├── ErrorResponseTest.java
                                └── StudentResponseTest.java
```

---

## 📚 Architecture

This project follows **Hexagonal Architecture** (Ports & Adapters) and **Domain-Driven Design** principles:

```
┌─────────────────────────────────────────────────┐
│              INFRASTRUCTURE                      │
│    REST API, Database, Specifications            │
│                                                  │
│    ┌───────────────────────────────────────┐     │
│    │          APPLICATION                  │     │
│    │   Services, Commands, Exceptions      │     │
│    │                                       │     │
│    │    ┌───────────────────────────┐      │     │
│    │    │         DOMAIN            │      │     │
│    │    │  Entities, VOs, Enums     │      │     │
│    │    │  Query abstractions       │      │     │
│    │    └───────────────────────────┘      │     │
│    └───────────────────────────────────────┘     │
└─────────────────────────────────────────────────┘
```

**Dependency rule:** Dependencies always point inward. The domain knows nothing about the outside world.

### Domain Layer (Core)
Pure business logic with no framework dependencies. Contains the Student aggregate root, 6 value objects with self-validation, 6 enums (including Degree with 22 university degrees), query abstractions (Pagination, PageResult, StudentSearchCriteria), and the repository interface (port).

### Application Layer (Orchestration)
Coordinates operations between the outside world and the domain. Contains the StudentService, command objects, and application-specific exceptions with support for multiple validation errors via BusinessViolation.

### Infrastructure Layer (Technical Details)
Implements the technical concerns: REST controllers with global exception handling and unified error format (ApiError), JPA persistence with bidirectional mapping and JPA Specifications for dynamic filtering, Spring Data repositories, REST DTOs, Swagger UI, CORS configuration, and database health monitoring.

### Request Flow

```
HTTP Request
    → StudentController (validate + delegate)
        → StudentService (orchestrate + Degree.fromString())
            → Domain (business logic)
                → StudentRepository port
                    → StudentRepositoryImpl adapter
                        → Specifications + Pageable
                            → Spring Data JPA → PostgreSQL
    ← PagedStudentResponse / StudentResponse (degree + degreeName)
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
                               │  StudentSpecifications   │
                               │  StudentJpaEntity        │
                               │  StudentJpaMapper        │
                               └──────────────────────────┘
```

---

## 🎯 Domain Model

### Student Entity (Aggregate Root)

The `Student` entity is the aggregate root with the following structure:

**Immutable fields:** DNI, full name, degree, current year, creation timestamp
**Mutable fields:** Email, phone, alumni info, RGPD consent, discovery/contact channels, notes
**Soft delete fields:** active (boolean), deletedAt (timestamp)

**Business operations:**
- Email/Phone management (add, update, remove, atomic update)
- RGPD consent tracking (3 signature methods + pending)
- Alumni status management (mark/unmark with type and year)
- Soft delete (deactivate with DNI reuse support)

Built using the **Builder pattern** with intelligent defaults.

---

### Value Objects

| Value Object | Purpose | Key Validation |
|---|---|---|
| **Dni** | Spanish DNI/NIE | MOD 23 algorithm, letter validation |
| **Email** | Email address | Format validation, lowercase normalization |
| **Phone** | Phone number | E.164 format, auto +34 prefix |
| **FullName** | Person's name | Name + first surname required |
| **AlumniInfo** | Alumni status | If alumni → type + year required |
| **RgpdConsent** | GDPR consent | 4 statuses with different required fields |

All value objects are **immutable**, **self-validated**, and created via **factory methods**.

---

### Enums

#### Degree (22 values)
University degrees with `displayName` and `fromString()` factory method. Includes: `ELECTRONIC_ENGINEERING`, `COMPUTER_ENGINEERING`, `MECHANICAL_ENGINEERING`, `NURSING`, `PHYSIOTHERAPY`, `NUTRITION`, and 16 more.

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

| Layer | Tests | Type |
|---|---|---|
| Domain (VOs + Entity) | 110 | Unit |
| Domain (Query) | 9 | Unit |
| Application (Service) | 16 | Unit (Mockito) |
| Infrastructure (Repository) | 21 | Integration (PostgreSQL) |
| Infrastructure (Controller) | 22 | Web (MockMvc) |
| Infrastructure (DTOs) | 22 | Unit |
| Context | 1 | Integration |

**Total: 210 tests** — all passing.

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=StudentTest

# Run specific layer
mvn test -Dtest="com.orientation.backend.users.domain.**"
```

**Note:** Integration tests require PostgreSQL running (via Docker).

---

## 🐳 Docker Commands

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# Remove volumes (clean database)
docker-compose down -v
```

---

## 🗺️ Roadmap

### Sprint 1 ✅

- [x] **Task #7:** Student Domain Model — 110 unit tests
- [x] **Task #8:** Student Repository Layer — 11 integration tests
- [x] **Task #9:** Student Application Layer — 13 service tests
- [x] **Task #10:** REST API Controllers — 14 controller tests

### Sprint 2 ✅

- [x] **Task #11:** Soft Delete — Deactivation, partial unique index, DNI reuse
- [x] **Task #12:** Pagination & Filtering — JPA Specifications, composable filters, paginated responses
- [x] **Task #13:** Degree as Enum — 22 university degrees, fromString(), displayName, filter support
- [x] **Exception Handling Refactoring** — Unified ApiError format, multiple validation errors, architectural fix

### Sprint 2 — Pending

- [ ] Testcontainers migration

### Upcoming
- [ ] **Sprint 3:** Sessions Module
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
| Dynamic Filtering | JPA Specifications (Criteria API) |
| API Docs | springdoc-openapi (Swagger UI) |
| Testing | JUnit 5 + Mockito + AssertJ + MockMvc |
| Containers | Docker + Docker Compose |
| Monitoring | Spring Boot Actuator |

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Authors

**Toni Romero** — [@ToniR90](https://github.com/ToniR90)

**Daniel Mata** — [@DanielMataC](https://github.com/DanielMataC)

---

**Last Updated:** March 2026