# 🗺️ Sprint Roadmap - Student Management System

**Project**: University Student Management Backend  
**Architecture**: Modular Monolith + Hexagonal per Module  
**Availability**: 20h/week (media jornada)  
**Sprint Duration**: 2 weeks per sprint  

---

## 📊 Sprint Overview

| Sprint     | Goal                                  | Duration | Status       |
|------------|----------------------------------------|----------|--------------|
| Sprint 1   | Student CREATE + READ                  | 2 weeks  | 🟡 In Progress |
| Sprint 2   | Student UPDATE + DELETE + Refactoring  | 2 weeks  | ⏳ Planned     |
| Sprint 3   | Sessions Module - Basic CRUD           | 2 weeks  | ⏳ Planned     |
| Sprint 4   | Authentication (JWT)                   | 2 weeks  | ⏳ Planned     |

---

## 🎯 Sprint 1 – Student CREATE + READ (Current)

**User Story**  
Como orientador, quiero poder registrar nuevos estudiantes y consultar la lista de estudiantes registrados.

### ✅ Key Deliverables
- Spring Boot application running  
- PostgreSQL connected via Docker  
- Student domain model implemented  
- Student repository (hexagonal architecture)  
- REST API: POST + GET endpoints  
- Basic tests passing  
- README with setup instructions  

### 📌 Scope

**IN SCOPE**
- Create student (POST)
- List all students (GET)
- Get student by DNI (GET)
- Basic validations (unique DNI/email, required fields)
- Repository layer with hexagonal architecture
- Basic exception handling
- Unit + Integration tests (>60% coverage)

**OUT OF SCOPE (deferred to Sprint 2)**
- UPDATE operation
- DELETE operation
- Service/UseCase layer (controller → repository directly)
- Advanced validation rules
- Pagination
- Complete exception handling
- Detailed API documentation

### 🧩 Tasks Breakdown

| # | Task                             | Estimation | Priority  | Status     |
|---|----------------------------------|------------|-----------|------------|
| 4 | Structure investigation          | 6h         | CRITICAL  | ✅ DONE     |
| 5 | Project Setup & Dependencies     | 4h         | CRITICAL  | ⏳ TODO     |
| 6 | Database Configuration           | 2h         | CRITICAL  | ⏳ TODO     |
| 7 | Student Domain Model             | 3h         | CRITICAL  | ⏳ TODO     |
| 8 | Student Repository               | 3h         | CRITICAL  | ⏳ TODO     |
|10 | Student DTOs                     | 2h         | CRITICAL  | ⏳ TODO     |
|11 | Student Controller (MVP)         | 3h         | CRITICAL  | ⏳ TODO     |
|15A| Basic Tests                      | 3h         | HIGH      | ⏳ TODO     |
|13A| Basic Exception Handler          | 2h         | MEDIUM    | ⏳ TODO     |
|14A| Swagger Basic Setup              | 1h         | LOW       | ⏳ TODO     |

**TOTAL**: 29 hours (within 32h sprint capacity with buffer)

### ⚠️ Technical Debt Accepted (to be resolved in Sprint 2)
- Controller calls repository directly (no service layer)
- Basic exception handling only
- No pagination
- Test coverage ~60% (not 80%)

---

## 🚀 Sprint 2 – Student UPDATE + DELETE + Refactoring

**User Story**  
Como orientador, quiero poder actualizar y eliminar estudiantes, para mantener la información actualizada y gestionar datos obsoletos.

### ✅ Key Deliverables
- Student UPDATE endpoint (PUT)
- Student DELETE endpoint (DELETE)
- Service/UseCase layer refactoring
- Advanced validation rules
- Robust exception handling
- Pagination in GET list
- Increased test coverage (>80%)
- Complete API documentation (Swagger)

### 📌 Scope

**IN SCOPE**
- Update student (PUT /api/v1/students/{dni})
- Delete student (DELETE /api/v1/students/{dni})
- Refactor: Add UseCase layer (CreateStudentUseCase, GetStudentUseCase)
- Advanced validations (business rules)
- Custom exception hierarchy
- Error response standardization
- Pagination (GET /api/v1/students?page=0&size=20)
- Search by name (optional)
- Complete Swagger documentation
- Increase test coverage to >80%

**OUT OF SCOPE**
- Sessions module
- Authentication/Authorization
- Collaborators entity

### 🧩 Tasks Breakdown (Preliminary)

| #    | Task                                 | Estimation |
|------|--------------------------------------|------------|
| 9    | Refactor: Add UseCase Layer          | 4h         |
|11B   | Student Controller – UPDATE endpoint | 2h         |
|11C   | Student Controller – DELETE endpoint | 2h         |
| 12   | Advanced Validation Rules            | 3h         |
|13B   | Custom Exception Hierarchy           | 3h         |
|14B   | Complete Swagger Documentation       | 2h         |
| 16   | Pagination Implementation            | 3h         |
| 17   | Search Functionality (optional)      | 3h         |
|15B   | Complete Test Suite (>80% coverage)  | 4h         |
| 18   | Code Review & Refactoring            | 2h         |

**TOTAL**: ~28 hours

---

## 📅 Sprint 3 – Sessions Module (Basic CRUD)

**User Story**  
Como orientador, quiero poder registrar sesiones individuales con estudiantes, para llevar un histórico de las sesiones realizadas.

### ✅ Key Deliverables
- Session domain model
- Session repository
- REST API: CRUD operations for sessions
- Relationship: Session → Student (one-to-many)
- Tests for sessions module

### 📌 Scope (Preliminary)
- Session entity (id, student_id, date, type, notes)
- CRUD endpoints for sessions
- Link session to student
- List sessions by student
- Basic validations

**ESTIMATION**: 25–30h

---

## 🔐 Sprint 4 – Authentication (JWT)

**User Story**  
Como orientador, quiero poder autenticarme en el sistema para acceder de forma segura a la gestión de estudiantes y sesiones.

### ✅ Key Deliverables
- Auth module implementation (as per ADD)
- JWT token generation and validation
- Login endpoint
- Security filter for protected endpoints
- Orientador entity and repository

### 📌 Scope (Preliminary)
- Orientador entity
- Login endpoint (POST /auth/login)
- JWT token generation
- JwtAuthenticationFilter
- Security configuration
- Protected endpoints (all except /auth/login and /health)
- Tests for authentication flow

**ESTIMATION**: 25–30h

---

## 📏 Sprint Metrics & Learning Goals

### Velocity Tracking

| Sprint   | Planned (h) | Actual (h) | Completed Tasks | Notes        |
|----------|-------------|------------|------------------|--------------|
| Sprint 1 | 29h         | TBD        | TBD / 10         |              |
| Sprint 2 | 28h         | --         | -- / 10          |              |
| Sprint 3 | 28h         | --         | TBD              |              |
| Sprint 4 | 28h         | --         | TBD              |              |

### Learning Objectives by Sprint

**Sprint 1**
- ✅ Spring Boot project structure
- ✅ Hexagonal architecture implementation
- ✅ JPA + Hibernate basics
- ✅ REST API design
- ✅ Docker for databases
- ✅ Testcontainers

**Sprint 2**
- UseCase pattern implementation
- Custom exception handling
- Pagination strategies
- Test coverage optimization
- Code refactoring techniques

**Sprint 3**
- Entity relationships (JPA)
- Cross-module communication
- Domain events (optional)
- Complex queries

**Sprint 4**
- Spring Security configuration
- JWT implementation
- Authentication flows
- Security testing

---

## 🎓 Technical Debt & Improvement Backlog

### Technical Debt Created in Sprint 1
- Controller calls repository directly (no service layer)
- Basic exception handling only
- No pagination
- Test coverage ~60%
- No API versioning strategy
- No logging strategy

### Resolved in Sprint 2
- Add UseCase layer
- Improve exception handling
- Add pagination
- Increase test coverage to >80%

### Future Improvements (Post-Sprint 4)
- Add audit trail (createdBy, updatedBy)
- Add soft delete
- Add API rate limiting
- Add caching strategy
- Add observability (metrics, tracing)
- Consider CQRS if needed

---

## 📝 Sprint Retrospective Template

After each sprint, document:

**What Went Well? ✅**  
...

**What Could Be Improved? ⚠️**  
...

**Action Items for Next Sprint 🎯**  
...

**Technical Learnings 🎓**  
...

---

## 🚀 Next Steps

**Current Focus**: Sprint 1 – Task #5 (Project Setup & Dependencies)

**Before Starting Next Task**
- Review ADD document
- Ensure Docker is installed
- Ensure Maven/Java 17+ installed
- Create Git branch for Sprint 1

**When Sprint 1 is Complete**
- Sprint Review (demo to yourself or PO)
- Sprint Retrospective
- Update velocity metrics
- Plan Sprint 2 in detail

---

**Last Updated**: 2025-01-XX