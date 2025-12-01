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
│   │       ├── users/          # User management module
│   │       ├── sessions/       # Session management module
│   │       ├── auth/           # Authentication module (Sprint 4)
│   │       └── shared/
│   │           └── infrastructure/
│   │               └── config/ # Shared configuration
│   └── resources/
│       ├── application.yml     # Application configuration
│       └── db/
│           └── migration/      # Flyway migrations
└── test/
    └── java/
        └── com/orientation/backend/
```

---

## 🧪 Running Tests
```bash
# Run all tests
mvn test

# Run tests with coverage
mvn clean test jacoco:report
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
- **JUnit 5**
- **Spring Boot Test**
- **Testcontainers** (integration tests)

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

This project follows **Hexagonal Architecture** (Ports & Adapters) principles:

- **Domain Layer:** Pure business logic, no framework dependencies
- **Application Layer:** Use cases, orchestration
- **Infrastructure Layer:** Technical implementations (DB, REST, etc.)

### Design Principles

- ✅ **SOLID principles**
- ✅ **Package by feature** (modular monolith)
- ✅ **Dependency inversion** (domain doesn't depend on infrastructure)
- ✅ **Separation of concerns**

---

## 🗺️ Roadmap

- [x] **Sprint 1:** Student CRUD (Create & Read)
- [ ] **Sprint 2:** Student CRUD (Update & Delete) + Refactoring
- [ ] **Sprint 3:** Sessions Module
- [ ] **Sprint 4:** Authentication (JWT)

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