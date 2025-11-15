# 🏗️ Architecture Decisions Document (ADD)

This document defines the foundational architectural decisions for the university management backend system.  
It ensures consistency, scalability, and clarity throughout the development lifecycle.

---

## 1. Architecture Pattern

### **Chosen Pattern: Modular Monolith with Hexagonal Architecture per Module**

### **Rationale**
- The project is starting as a single deployable unit, which simplifies DevOps, testing, and coordination.
- We expect significant feature expansion (students, collaborators, sessions, notifications,...).  
  A modular monolith prevents the codebase from becoming a “big ball of mud”.
- Hexagonal Architecture isolates business logic from frameworks (Spring), enabling:
  - Easier unit testing  
  - Cleaner separation of concerns  
  - Reduced coupling to Spring Boot or any specific database  
- Each module encapsulates **domain**, **application** and **infrastructure**, allowing parallel development if needed.

### **Future Migration Path**
If the system grows enough, each module can evolve into a microservice without rewriting business logic, because boundaries are already clean and explicit.

---

## 2. Package Structure

### **Chosen Structure: Package by Feature (Feature-Modular Structure)**

Instead of grouping files by technical type (controller, service…), we group them by **domain feature**, which aligns with the business language and reduces coupling across features.
com.orientacion/
├── users/
│ ├── domain/
│ ├── application/
│ └── infrastructure/
├── sessions/
│ ├── domain/
│ ├── application/
│ └── infrastructure/
└──shared/
  ├── domain/          # Value Objects comunes, excepciones de dominio base
  ├── application/     # DTOs base, interfaces de servicios transversales
  └── infrastructure/  # Config global, exception handlers, logging


### **Hexagonal Structure Inside Each Module**

module/
├── domain/
│ ├── entities/
│ ├── valueobjects/
│ ├── services/
│ ├── repository/   # Interfaces (ports)
│ └── events/
├── application/
│ ├── usecases/      # Orchestration logic
│ ├── dto/           # Request/Response objects
│ └── ports/         # Outbound ports for external services
└── infrastructure/
├── persistence/  # Implementations
├── rest/ (Controllers)
├── mappers/
└── config/

### **Repository Layer Example**

#### Domain Layer (Port)
```java
// domain/repository/StudentRepository.java
public interface StudentRepository {
    Student save(Student student);
    Optional<Student> findById(StudentId id);
    List<Student> findAll();
}
```

#### Infrastructure Layer (Adapter)
```java
// infrastructure/persistence/JpaStudentRepository.java
@Repository
public class JpaStudentRepository implements StudentRepository {
    @Autowired
    private StudentJpaDataRepository jpaDataRepository;
    
    @Override
    public Student save(Student student) {
        StudentEntity entity = mapper.toEntity(student);
        StudentEntity saved = jpaDataRepository.save(entity);
        return mapper.toDomain(saved);
    }
}

// infrastructure/persistence/StudentJpaDataRepository.java
public interface StudentSpringDataRepository extends JpaRepository<StudentEntity, Long> {
}
```


### **Justification**
- Follows DDD-like modularity.
- High cohesion + low coupling.
- Clear separation between business rules and technical details.
- Improves maintainability and readability in large systems.

---

## 3. Naming Conventions

### **Domain Layer**
- Entities: `Student`, `Session`, `Collaborator`
- Value Objects: `Email`, `Dni`, `SessionDate`
- Repository Interfaces: `{Entity}Repository`

### **Application Layer**
- Use cases: `{Action}{Entity}UseCase`  
  - Example: `CreateStudentUseCase`
- DTOs: `{Entity}{Action}Request`, `{Entity}Response`
- Mappers: `{Entity}Mapper`

### **Infrastructure Layer**
- REST Controllers: `{Entity}Controller`
- Repository Implementations: `{Entity}RepositoryImpl`
  - Example: `StudentRepositoryImpl implements StudentRepository`
- Config: `PersistenceConfig`, `SwaggerConfig`, etc.

### **General Rules**
- Class names are singular unless they represent collections.
- Avoid abbreviations except for very standard ones (DTO, ID…).

### **Language Strategy**
- **Code:** English (classes, variables, methods, comments)
- **Documentation:** Spanish (ADDs, user stories, PO communication)
- **Domain Terms:** Keep Spanish terms if they're ubiquitous language
  - Example: `Orientador` class is acceptable if that's the exact business term
  - But prefer English equivalents when natural: `Student` over `Estudiante`

### **Auth Module Specific**
- Token Service Interface: `TokenService`
- Token Service Implementation: `JwtTokenService`
- Authentication Filter: `JwtAuthenticationFilter`
- Security Config: `SecurityConfig`
- Auth DTOs: `LoginRequest`, `LoginResponse`, `RefreshTokenRequest`

---

## 4. Database Strategy

### **Development Environment**
- **PostgreSQL 15+** in Docker Compose  
- Testcontainers recommended for integration tests.

### **Production Environment**
- PostgreSQL on Render (or any managed service)

### **Rationale**
- PostgreSQL is robust, ACID-compliant, and widely supported.
- JSONB and advanced indexing may be useful for analytics later.
- Using Docker ensures reproducible environments.
- Migration scripts will be handled with **Flyway**
  - Simpler than Liquibase for most use cases
  - Better Spring Boot integration
  - SQL-based migrations (easy to understand)

---

## 5. Module Boundaries

### **users module**
Handles:
- Student management
- Collaborator and faculty roles
- Common user validations (email uniqueness, DNI, etc.)

### **sessions module**
Handles:
- Individual sessions
- Group sessions
- Attendance records
- Scheduling rules

### **shared module**
Provides:
- Generic value objects (`Email`, `FullName`, etc.)
- Base exceptions
- Cross-module utilities (Date utilities, Result wrappers…)
- Optional: domain events implementation

### **auth module**
Handles:
- Orientador authentication (login)
- JWT token generation and validation
- Token refresh (Phase 2)
- Logout mechanism (Phase 2)
- Password management

---

## 6. Cross-Cutting Concerns

### **Error Handling**
- Global exception handler in `shared/infrastructure/rest/`.

### **Validation**
- Domain-first validation (Value Objects)
- Spring Validation for request DTOs

### **Logging**
- SLF4J + Logback with contextual logging per module.

### **Security**

#### **Access Control**
- **Users:** Only orientadores (counselors/administrators)
- **Access Level:** All authenticated users have full permissions (no role hierarchy)
- **Students/Collaborators:** Data entities only, no system access

#### **Authentication Strategy: JWT-Based**

**Chosen Approach:** JSON Web Tokens (JWT) with Spring Security

**Implementation Phases:**

**Phase 1 (Sprint 1): Basic JWT**
- Login endpoint (`POST /auth/login`) returns access token
- Token expiration: 24 hours
- Token validation filter on all protected endpoints
- No refresh token initially (simplified MVP)

**Phase 2 (Sprint 2-3): Complete JWT Flow**
- Refresh token implementation (7-day expiration)
- Token refresh endpoint (`POST /auth/refresh`)
- Logout mechanism (token invalidation)
- Proper error handling for expired/invalid tokens

#### **Technical Details**

**JWT Structure:**
```json
{
  "sub": "orientador_id",
  "username": "juan.garcia@university.edu",
  "iat": 1234567890,
  "exp": 1234654290
}
```

**Security Configuration:**
- Secret key stored in environment variables
- HMAC-SHA256 algorithm for signing
- Tokens sent via `Authorization: Bearer <token>` header

**Endpoints Protection:**
- **Public:** `/auth/login`, `/health`
- **Protected:** All other endpoints (require valid JWT)

#### **Rationale**
- **Industry Standard:** JWT is the de-facto standard for modern APIs
- **Stateless:** No server-side session storage needed (better scalability)
- **Frontend-Ready:** Compatible with any frontend framework (React, Angular, Vue)
- **Mobile-Ready:** Works seamlessly with mobile applications
- **Microservices-Ready:** Tokens can be validated by multiple services
- **Learning Value:** Essential skill for modern backend development

#### **Security Considerations**
- Tokens stored client-side (localStorage or httpOnly cookies)
- HTTPS required in production
- Short token expiration to limit exposure
- Refresh tokens for better UX without compromising security

#### **Future Enhancements**
- Multi-factor authentication (MFA)
- Role-based access control if permission levels emerge
- OAuth2 integration if third-party auth is needed

### **Auth Module Structure**
```
auth/
├── domain/
│   ├── entities/
│   │   └── Orientador.java
│   ├── valueobjects/
│   │   ├── Username.java
│   │   └── Password.java (hashed)
│   └── repository/
│       └── OrientadorRepository.java
├── application/
│   ├── usecases/
│   │   ├── LoginUseCase.java
│   │   ├── RefreshTokenUseCase.java (Phase 2)
│   │   └── LogoutUseCase.java (Phase 2)
│   ├── dto/
│   │   ├── LoginRequest.java
│   │   ├── LoginResponse.java (contains JWT)
│   │   └── RefreshTokenRequest.java
│   └── ports/
│       └── TokenService.java (interface)
└── infrastructure/
    ├── security/
    │   ├── JwtTokenService.java (implements TokenService)
    │   ├── JwtAuthenticationFilter.java
    │   └── SecurityConfig.java
    ├── rest/
    │   └── AuthController.java
    └── persistence/
        └── JpaOrientadorRepository.java
```

### **Required Dependencies**
```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT Library -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>

<!-- Password Encoding -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

### **Security Testing**

**Unit Tests:**
- `JwtTokenServiceTest`: Token generation, validation, expiration
- `LoginUseCaseTest`: Authentication logic with mocked repository

**Integration Tests:**
- `AuthControllerIntegrationTest`: Login flow end-to-end
- `JwtAuthenticationFilterTest`: Token validation in requests
- Test scenarios:
  - Valid credentials → returns token
  - Invalid credentials → 401 Unauthorized
  - Valid token → access granted
  - Expired token → 401 Unauthorized
  - No token → 401 Unauthorized
  - Malformed token → 401 Unauthorized

---

## 7. Future Architecture Extensions (Post-MVP)

**Note:** These are potential evolutions, not immediate requirements.  
The current architecture supports these paths without major rewrites.

- CQRS if read/write workloads become complex  
- Event-driven architecture (Kafka/RabbitMQ) if async communication is needed  
- Full DDD tactical patterns (aggregates, bounded contexts) as domain grows  
- Microservices extraction following module boundaries

---

## 8. Testing Strategy

### **Unit Tests**
- **Scope:** Domain entities, value objects, domain services
- **Tools:** JUnit 5, AssertJ
- **Isolation:** Pure Java, no Spring context
- **Example:** `EmailTest`, `StudentTest`, `CreateStudentUseCaseTest` (with mocked repos)

### **Integration Tests**
- **Scope:** Repository implementations, REST endpoints
- **Tools:** 
  - Spring Boot Test (`@SpringBootTest`)
  - Testcontainers (PostgreSQL container)
  - MockMvc (REST layer)
- **Example:** `JpaStudentRepositoryIntegrationTest`, `StudentControllerIntegrationTest`

### **Architecture Tests**
- **Tool:** ArchUnit (optional but recommended)
- **Validations:**
  - Domain doesn't depend on infrastructure
  - No circular dependencies between modules
  - Naming conventions enforcement

### **Coverage Goal**
- Domain layer: >80%
- Application layer: >70%
- Infrastructure layer: >60% (focus on critical paths)

---

## 9. Summary of Decisions
✓ Modular monolith as base  
✓ Hexagonal architecture per module  
✓ Package-by-feature organization  
✓ PostgreSQL with Flyway  
✓ Clear naming conventions  
✓ Proper domain boundaries  

This structure ensures the project starts simple but scalable, and remains easy to extend and maintain throughout its lifecycle.