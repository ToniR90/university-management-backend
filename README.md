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

### 2. Start PostgreSQL Database

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

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

**Startup time:** ~5-6 seconds

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

Or open in browser: http://localhost:8080/actuator/health

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
- Relaxed timeouts (10-30 minutes)

#### Test Profile
```bash
# Set environment variable (PowerShell)
$env:SPRING_PROFILES_ACTIVE="test"
mvn spring-boot:run

# Clean up after testing
Remove-Item Env:\SPRING_PROFILES_ACTIVE

# Or run tests (automatically uses test profile)
mvn test
```

**Features in test profile:**
- SQL queries NOT logged (cleaner test output)
- Minimal logging (WARN level)
- Health endpoint details hidden
- Connection pool: 5 max, 2 min-idle
- Leak detection disabled
- Fast timeouts (1-10 minutes)

#### Switching Profiles

You can also set the active profile via environment variable:
```bash
# Linux/macOS
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run

# Windows (CMD)
set SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run

# Windows (PowerShell)
$env:SPRING_PROFILES_ACTIVE="dev"
mvn spring-boot:run
```

Or in your IDE:
- **IntelliJ IDEA:** Run Configuration → Environment Variables → `SPRING_PROFILES_ACTIVE=dev`
- **VS Code:** launch.json → `"env": {"SPRING_PROFILES_ACTIVE": "dev"}`

### Profile Configuration Files
```
src/main/resources/
├── application.yml          # Base configuration (all profiles)
├── application-dev.yml      # Development overrides
└── application-test.yml     # Test overrides
```

### Configuration Highlights

#### HikariCP Connection Pool

The application uses HikariCP for efficient database connection pooling:

**Development:**
- Maximum pool size: 10 connections
- Minimum idle: 5 connections
- Leak detection: 30 seconds (helps identify connection leaks during development)
- Relaxed timeouts: idle (10 min), max lifetime (30 min)

**Test:**
- Maximum pool size: 5 connections
- Minimum idle: 2 connections
- Leak detection: disabled (prevents false positives in tests)
- Fast timeouts: idle (1 min), max lifetime (10 min)

#### Custom Health Indicator

The application includes a custom database health indicator accessible at `/actuator/health`:
```bash
curl http://localhost:8080/actuator/health
```

**Response (dev profile):**
```json
{
  "status": "UP",
  "components": {
    "database": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "version": "15.15",
        "driver": "PostgreSQL JDBC Driver",
        "schema": "public"
      }
    },
    "db": {...},
    "diskSpace": {...},
    "ping": {...}
  }
}
```

**Note:** Health details are only shown in dev profile. In test/prod, only the status is visible for security.

---

## 🏗️ Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/orientation/backend/
│   │       ├── StudentManagementSystemApplication.java
│   │       ├── users/
│   │       │   └── domain/
│   │       │       └── model/
│   │       │           ├── entities/
│   │       │           │   └── Student.java          # Aggregate Root
│   │       │           ├── valueobjects/
│   │       │           │   ├── Email.java            # RFC 5322 validation
│   │       │           │   ├── Phone.java            # E.164 format
│   │       │           │   ├── Dni.java              # Spanish DNI/NIE (MOD 23)
│   │       │           │   ├── FullName.java         # Composite VO
│   │       │           │   ├── AlumniInfo.java       # Alumni status
│   │       │           │   └── RgpdConsent.java      # GDPR consent
│   │       │           └── enums/
│   │       │               ├── AlumniType.java
│   │       │               ├── ContactMethod.java
│   │       │               ├── CurrentYear.java
│   │       │               ├── DiscoveryChannel.java
│   │       │               └── RgpdConsentStatus.java
│   │       ├── sessions/       # Session management (Sprint 3)
│   │       ├── auth/           # Authentication (Sprint 4)
│   │       └── shared/
│   │           └── infrastructure/
│   │               └── config/
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-test.yml
│       └── db/
│           └── migration/
│               └── V1__create_students_table.sql
└── test/
    └── java/
        └── com/orientation/backend/
            └── users/
                └── domain/
                    └── model/
                        ├── valueobjects/
                        │   ├── EmailTest.java        (9 tests)
                        │   ├── PhoneTest.java        (12 tests)
                        │   ├── DniTest.java          (23 tests)
                        │   ├── FullNameTest.java     (14 tests)
                        │   ├── AlumniInfoTest.java   (11 tests)
                        │   └── RgpdConsentTest.java  (10 tests)
                        └── entities/
                            └── StudentTest.java      (31 tests)
```

---

## 🎯 Domain Model

### Student Entity (Aggregate Root)

The `Student` entity is the aggregate root representing a university student with comprehensive information management.

**Core Fields:**
- **Identity:** DNI/NIE (Spanish identification), unique ID
- **Personal Info:** Full name (name, first surname, optional second surname)
- **Contact:** Email, phone (both optional)
- **Academic:** Degree name, current year (FIRST through FOURTH)
- **Alumni Status:** Alumni type (Bachelor, Master, Doctorate, Erasmus), graduation year
- **GDPR Compliance:** Consent status (Pending, Signed in Person, Signed Online, Already Signed)
- **Tracking:** Discovery channel, contact method, counselor notes
- **Metadata:** Creation and last update timestamps

**Business Capabilities:**
- Email/Phone management (add, update, remove with validation)
- Atomic contact info updates (email + phone together)
- GDPR consent tracking (3 signature methods)
- Alumni status management (mark/unmark)
- Discovery and contact channel registration
- Counselor notes management

---

### Value Objects

#### 1. Email
**Purpose:** Validate and normalize email addresses

**Features:**
- RFC 5322 compliant validation
- Automatic normalization to lowercase
- Immutable

**Validation:**
- Valid format (user@domain.com)
- Non-null, non-empty
- Proper domain structure

**Example:**
```java
Email email = Email.of("student@university.edu");
// Normalized: "student@university.edu"
```

---

#### 2. Phone
**Purpose:** Validate and normalize Spanish phone numbers

**Features:**
- E.164 international format
- Automatic +34 prefix addition for Spanish numbers
- Support for mobile (6xx, 7xx) and landline (8xx, 9xx)
- Immutable

**Validation:**
- 9 digits (Spanish format)
- Valid prefix (6, 7, 8, 9)
- Automatic normalization with spaces removed

**Examples:**
```java
Phone.of("600123456")     // → "+34600123456"
Phone.of("+34912345678")  // → "+34912345678"
Phone.of("912 345 678")   // → "+34912345678" (spaces removed)
```

---

#### 3. Dni (Spanish ID)
**Purpose:** Validate Spanish DNI (National ID) and NIE (Foreigner ID)

**Features:**
- MOD 23 algorithm validation
- Automatic NIE conversion (X→0, Y→1, Z→2)
- Automatic normalization (uppercase, no spaces/dashes)
- Distinguishes between DNI and NIE
- Immutable

**Validation:**
- 8 digits + 1 letter
- Correct letter according to MOD 23 algorithm
- NIE must start with X, Y, or Z
- DNI must start with digit

**Examples:**
```java
Dni.of("12345678Z")    // Valid DNI
Dni.of("X1234567L")    // Valid NIE (X converts to 0 for validation)
Dni.of("12345678-Z")   // Normalized to "12345678Z"
Dni.of("12345678z")    // Normalized to "12345678Z"
```

**NIE Conversion Table:**
- X → 0 (e.g., X1234567 becomes 01234567 for validation)
- Y → 1 (e.g., Y1234567 becomes 11234567 for validation)
- Z → 2 (e.g., Z1234567 becomes 21234567 for validation)

---

#### 4. FullName
**Purpose:** Represent a person's complete name

**Features:**
- Composite Value Object (name, first surname, second surname)
- Second surname is optional
- Automatic trimming of whitespace
- Immutable

**Validation:**
- Name: required, non-empty
- First surname: required, non-empty
- Second surname: optional (can be null)

**Methods:**
- `getFullName()`: Returns concatenated full name

**Examples:**
```java
FullName.of("Juan", "García", "López")
// getFullName() → "Juan García López"

FullName.of("María", "Martínez", null)
// getFullName() → "María Martínez"
```

---

#### 5. AlumniInfo
**Purpose:** Track student's alumni status

**Features:**
- Factory methods for type-safe creation
- Consistency validation (if alumni, requires type and year)
- Immutable

**Factory Methods:**
- `AlumniInfo.notAlumni()` → Not an alumni
- `AlumniInfo.createAlumni(type, year)` → Alumni with graduation info

**Validation:**
- Graduation year: >= 1900 and <= current year
- Type and year required together for alumni
- Type and year empty for non-alumni

**Example:**
```java
AlumniInfo.notAlumni()
// isAlumni() → false

AlumniInfo.createAlumni(AlumniType.BACHELOR, 2023)
// isAlumni() → true
// getType() → Optional[BACHELOR]
// getGraduationYear() → Optional[2023]
```

---

#### 6. RgpdConsent (GDPR Consent)
**Purpose:** Track GDPR consent compliance

**Features:**
- 4 different consent statuses
- Factory methods for each signature type
- Automatic timestamp/year tracking
- Immutable

**Factory Methods:**
- `RgpdConsent.pending()` → Awaiting consent
- `RgpdConsent.signedInPerson()` → Signed in person (with timestamp)
- `RgpdConsent.signedOnline()` → Signed online (with timestamp)
- `RgpdConsent.alreadySigned(year)` → Previously signed (with year)

**Status Types:**
- PENDING: No consent given yet
- SIGNED_IN_PERSON: Physically signed (stores signedDate)
- SIGNED_ONLINE: Digitally signed (stores signedDate)
- ALREADY_SIGNED: Pre-existing consent (stores signedYear)

**Validation:**
- Signed year: >= 2018 (GDPR effective date) and <= current year
- SignedDate automatically set for in-person/online
- SignedYear required for already-signed

**Examples:**
```java
RgpdConsent.pending()
// getStatus() → PENDING

RgpdConsent.signedInPerson()
// getStatus() → SIGNED_IN_PERSON
// getSignedDate() → Optional[2025-01-15T10:30:00]

RgpdConsent.alreadySigned(2020)
// getStatus() → ALREADY_SIGNED
// getSignedYear() → Optional[2020]
```

---

### Enums

#### AlumniType
Types of alumni status:
- `BACHELOR` - Bachelor's degree graduate
- `MASTER` - Master's degree graduate
- `DOCTORATE` - Doctorate degree graduate
- `ERASMUS` - Erasmus exchange program participant

#### ContactMethod
Ways students contacted the orientation service:
- `EMAIL` - Via email
- `PHONE` - Via phone call
- `IN_PERSON` - In-person visit
- `WHATSAPP` - Via WhatsApp
- `INSTAGRAM` - Via Instagram DM
- `OTHER` - Other methods

#### CurrentYear
Academic year levels:
- `FIRST` - First year
- `SECOND` - Second year
- `THIRD` - Third year
- `FOURTH` - Fourth year

#### DiscoveryChannel
How students discovered the orientation service:
- `WEBSITE` - University website
- `INSTAGRAM` - Instagram social media
- `FRIEND` - Friend recommendation
- `PROFESSOR` - Professor recommendation
- `EMAIL` - Email campaign
- `OTHER` - Other sources

#### RgpdConsentStatus
GDPR consent states:
- `PENDING` - Awaiting consent
- `SIGNED_IN_PERSON` - Signed physically
- `SIGNED_ONLINE` - Signed digitally
- `ALREADY_SIGNED` - Previously consented

---

### Design Patterns Used

#### Value Objects
All value objects are **immutable** and use:
- **Factory Method pattern:** `of()` static method for creation
- **Validation on construction:** Fail-fast principle
- **Equals by value:** Two VOs with same value are equal
- **No setters:** Immutable after creation

#### Aggregate Root (Student)
- **Builder pattern:** Fluent API for object construction
- **Rich Domain Model:** Business logic in the domain
- **Encapsulation:** Private setters, public business methods
- **Invariant protection:** Validates state transitions

#### Factory Methods (AlumniInfo, RgpdConsent)
- **Type-safe creation:** Different methods for different states
- **Self-documenting:** Method names express intent
- **Consistency guarantee:** Ensures valid state combinations

---

## 🧪 Testing

### Test Coverage

The domain layer has **>80% test coverage** with **110 unit tests**:

**Value Objects (79 tests):**
- EmailTest: 9 tests (RFC 5322 validation, normalization)
- PhoneTest: 12 tests (E.164 format, prefix handling)
- DniTest: 23 tests (MOD 23 algorithm, DNI/NIE validation)
- FullNameTest: 14 tests (composite VO, Optional handling)
- AlumniInfoTest: 11 tests (consistency rules, year validation)
- RgpdConsentTest: 10 tests (factory methods, status transitions)

**Entities (31 tests):**
- StudentTest: 31 tests (builder, business logic, equals/hashcode)

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=StudentTest

# Run tests with coverage report
mvn clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Test Structure

All tests follow the **AAA pattern** (Arrange-Act-Assert):
```java
@Test
void shouldAddEmail() {
    // Arrange
    Student student = createStudent();
    Email email = Email.of("test@test.com");

    // Act
    student.addEmail(email);

    // Assert
    assertTrue(student.getEmail().isPresent());
    assertEquals(email, student.getEmail().get());
}
```

---

## 🛠️ Tech Stack

### Core
- **Java 21** (LTS)
- **Spring Boot 3.3.3**
- **Maven 3.9**

### Database
- **PostgreSQL 15+**
- **Flyway** (database migrations)
- **Spring Data JPA** / **Hibernate**

### Development Tools
- **Spring Boot DevTools** (hot reload)
- **Lombok** (reduce boilerplate)
- **Docker Compose** (local development)

### Testing
- **JUnit 5** (110 unit tests)
- **Spring Boot Test**
- **Testcontainers** (integration tests - Sprint 2)

### Monitoring
- **Spring Boot Actuator** (health checks, metrics)

---

## 🐳 Docker Commands

### Start Services
```bash
docker-compose up -d
```

### Stop Services
```bash
docker-compose down
```

### View Logs
```bash
# All services
docker-compose logs

# Specific service
docker-compose logs postgres
docker-compose logs pgadmin
```

### Restart Services
```bash
docker-compose restart
```

### Remove Volumes (Clean Database)
```bash
docker-compose down -v
```

---

## 🔧 Troubleshooting

### Port 5432 Already in Use

**Check what's using the port:**
```bash
# macOS/Linux
lsof -i :5432

# Windows
netstat -ano | findstr :5432
```

**Solutions:**
1. Stop the conflicting service
2. Change port in `docker-compose.yml`:
```yaml
   ports:
     - "5433:5432"  # Use 5433 on host
```
And update `.env`:
```
   DB_URL=jdbc:postgresql://localhost:5433/students_db
```

### Application Fails to Start

1. **Check Docker is running:**
```bash
   docker ps
```

2. **Check PostgreSQL logs:**
```bash
   docker-compose logs postgres
```

3. **Verify Java version:**
```bash
   java -version  # Must be 21+
```

4. **Clean and rebuild:**
```bash
   mvn clean install
```

### Flyway Migration Fails

1. **Check database exists:**
    - Connect via pgAdmin or psql
    - Verify `students_db` database exists

2. **Check Flyway configuration:**
    - Open `src/main/resources/application.yml`
    - Verify Flyway settings

3. **Verify migration folder:**
    - Check `src/main/resources/db/migration/` exists

### Connection Refused Error

**Error:** `Connection to localhost:5432 refused`

**Solution:** Wait 10-15 seconds after `docker-compose up -d` for PostgreSQL to fully initialize.

Check health status:
```bash
docker ps  # STATUS should show "(healthy)"
```

---

## 📚 Architecture

This project follows **Hexagonal Architecture** (Ports & Adapters) and **Domain-Driven Design** principles:

### Layers

**Domain Layer (Core):**
- Pure business logic
- No framework dependencies
- Value Objects, Entities, Aggregates
- Rich Domain Model with behavior
- Invariant protection

**Application Layer:**
- Use cases orchestration
- Application services
- DTOs and mappers
- Transaction boundaries

**Infrastructure Layer:**
- Technical implementations
- JPA repositories
- REST controllers
- Database configuration
- External integrations

### Design Principles

- ✅ **SOLID principles**
- ✅ **Domain-Driven Design (DDD)**
- ✅ **Hexagonal Architecture**
- ✅ **Package by feature** (modular monolith)
- ✅ **Dependency inversion** (domain independent)
- ✅ **Separation of concerns**
- ✅ **Immutability** (Value Objects)
- ✅ **Fail-fast validation**

### Domain Model Characteristics

**Value Objects:**
- Immutable
- Validated on construction
- Equals by value
- No identity
- Factory methods (`of()`)

**Entities:**
- Mutable state
- Identity-based equality
- Rich behavior
- Encapsulated invariants
- Builder pattern for construction

**Aggregates:**
- Consistency boundaries
- Transaction boundaries
- Aggregate Root (Student)
- Business rules enforcement

---

## 🗺️ Roadmap

### Completed
- [x] **Sprint 1 - Task #7:** Student Domain Model
   - [x] Database migration (Flyway)
   - [x] Domain enums (5 types)
   - [x] Value Objects (6 VOs with validation)
   - [x] Student Entity (Aggregate Root)
   - [x] Unit tests (110 tests, >80% coverage)
   - [x] Documentation

### In Progress
- [ ] **Sprint 1 - Task #8:** Student Repository Layer
   - [ ] JPA Entity mapping
   - [ ] Spring Data JPA Repository
   - [ ] Repository implementation tests

### Upcoming
- [ ] **Sprint 1 - Task #9:** Student Application Services
- [ ] **Sprint 1 - Task #10:** REST API Controllers
- [ ] **Sprint 2:** Update & Delete operations + Refactoring
- [ ] **Sprint 3:** Sessions Module (Collaborators + Sessions)
- [ ] **Sprint 4:** Authentication & Authorization (JWT)

---

## 📊 Project Metrics

**Current Status:**
- **Lines of Code (Domain):** ~1,500
- **Unit Tests:** 110
- **Test Coverage:** >80% (domain layer)
- **Value Objects:** 6
- **Entities:** 1 (Aggregate Root)
- **Enums:** 5
- **Database Tables:** 1 (students)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Toni Romero**

- GitHub: [@tonir90](https://github.com/tonir90)

---

## 🤝 Contributing

This is a personal learning project. Feedback and suggestions are welcome!

---

**Last Updated:** November 2025