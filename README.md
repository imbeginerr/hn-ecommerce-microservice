# HN E-Commerce Microservices Platform

A modern, scalable e-commerce platform built with **Spring Boot Microservices** architecture and **Vue.js 3** frontend.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.0-blue)
![Vue.js](https://img.shields.io/badge/Vue.js-3-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue)

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Services](#services)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Database Schema](#database-schema)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

HN E-Commerce is a production-ready microservices-based e-commerce platform designed for scalability, maintainability, and high performance. The platform supports complete e-commerce workflows including user management, product catalog, inventory tracking, and order processing.

### Key Features

- ✅ **Microservices Architecture** - Independently deployable services
- ✅ **Service Discovery** - Netflix Eureka for dynamic service registration
- ✅ **API Gateway** - Centralized routing and load balancing
- ✅ **JWT Authentication** - Secure token-based authentication
- ✅ **Role-Based Access Control (RBAC)** - Fine-grained permission system
- ✅ **Auto Permission Discovery** - Automatic permission registration from annotations
- ✅ **Soft Delete** - Data retention with logical deletion
- ✅ **Audit Trails** - Automatic tracking of created/updated records
- ✅ **Database Per Service** - Isolated data storage for each microservice

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         Client Layer                            │
│                    (Vue.js 3 Frontend)                          │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    API Gateway (Port 8080)                      │
│              Routes: /api/v1/{service}/**                       │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│              Service Registry (Eureka Server)                   │
│                      Port 8761                                  │
└────────────────────────┬────────────────────────────────────────┘
                         │
         ┌───────────────┼───────────────┬──────────────┐
         ▼               ▼               ▼              ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐ ┌─────────────┐
│ Core Service │ │   Product   │ │ Inventory  │ │   Order     │
│  Port 8081   │ │   Service   │ │  Service   │ │  Service    │
│              │ │  Port 8082  │ │ Port 8083  │ │ Port 8084   │
├──────────────┤ ├─────────────┤ ├────────────┤ ├─────────────┤
│ PostgreSQL   │ │ PostgreSQL  │ │PostgreSQL  │ │ PostgreSQL  │
│   :5432      │ │   :5433     │ │  :5435     │ │   :5434     │
└──────────────┘ └─────────────┘ └────────────┘ └─────────────┘
```

## 🔧 Services

### 1. **HN Common Service** (Shared Library)
Provides shared functionality across all microservices:
- Base entities (BaseEntity, ApiResponse, SearchCriteria)
- JWT authentication & authorization
- Security configurations
- Common utilities and helpers
- Custom annotations (@RequirePermission)

### 2. **HN Eureka Service** (Service Discovery)
- **Port:** 8761
- **Purpose:** Service registration and discovery
- **URL:** http://localhost:8761

### 3. **HN Gateway Service** (API Gateway)
- **Port:** 8080
- **Purpose:** Single entry point for all client requests
- **Features:**
  - Request routing
  - Load balancing
  - Circuit breaker patterns
- **Base URL:** http://localhost:8080/api/v1

### 4. **HN Core Service** (User Management)
- **Port:** 8081
- **Database:** hn_core_service (PostgreSQL :5432)
- **Responsibilities:**
  - User authentication (Login/Logout/Token refresh)
  - User CRUD operations
  - Role management
  - Permission management
  - JWT token generation and validation
  - Auto-discovery of permissions from @RequirePermission annotations

**Key Endpoints:**
```
POST   /auth/token           - User login
POST   /auth/refresh-token   - Refresh access token
POST   /auth/logout          - User logout
POST   /auth/introspect      - Validate token
GET    /user                 - List users
POST   /user                 - Create user
GET    /user/{id}            - Get user details
PUT    /user/{id}            - Update user
DELETE /user/{id}            - Delete user (soft delete)
POST   /user/restore/{id}    - Restore deleted user
GET    /user/myinfo          - Get current user info
```

### 5. **HN Product Service**
- **Port:** 8082
- **Database:** hn-product-service (PostgreSQL :5433)
- **Responsibilities:**
  - Product catalog management
  - Product categories
  - Product attributes and variants
  - Product search and filtering

### 6. **HN Inventory Service**
- **Port:** 8083
- **Database:** hn-inventory-service (PostgreSQL :5435)
- **Responsibilities:**
  - Stock level management
  - Warehouse management
  - Inventory tracking
  - Stock reservations

### 7. **HN Order Service**
- **Port:** 8084
- **Database:** hn-order-service (PostgreSQL :5434)
- **Responsibilities:**
  - Order creation and management
  - Order status tracking
  - Order history
  - Payment integration (planned)

## 💻 Tech Stack

### Backend
- **Java 21** - Programming language
- **Spring Boot 3.5.7** - Application framework
- **Spring Cloud 2025.0.0** - Microservices framework
  - Spring Cloud Gateway - API Gateway
  - Netflix Eureka - Service Discovery
  - Resilience4j - Circuit Breaker
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Relational database
- **MapStruct 1.5.5** - Object mapping
- **Lombok** - Boilerplate code reduction
- **Auth0 JWT 4.4.0** - JWT token handling
- **Hibernate JPA Metamodel Generator** - Type-safe queries

### Frontend
- **Vue.js 3** - Progressive JavaScript framework
- **Vue Router** - Client-side routing
- **Pinia/Vuex** - State management
- **Axios** - HTTP client

### DevOps & Tools
- **Maven** - Build automation
- **Docker** (planned) - Containerization
- **Git** - Version control

## 📦 Prerequisites

Before running this project, ensure you have:

- **Java 21** or higher
- **Maven 3.9+**
- **PostgreSQL 14+**
- **Node.js 18+** and **npm/yarn** (for frontend)
- **Git**

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/hn-ecommerce-microservice.git
cd hn-ecommerce-microservice
```

### 2. Setup Databases

Create PostgreSQL databases for each service:

```sql
CREATE DATABASE hn_core_service;
CREATE DATABASE "hn-product-service";
CREATE DATABASE "hn-inventory-service";
CREATE DATABASE "hn-order-service";
```

### 3. Configure Environment Variables

Create environment variables or update `application.yml` files:

```bash
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password
export SECRET_KEY=your-secret-key-min-256-bits
```

### 4. Build Common Service

The common service must be built and installed first as it's a dependency:

```bash
cd hn-common-service
mvn clean install
cd ..
```

### 5. Start Services in Order

#### a. Start Eureka Server (Service Discovery)
```bash
cd hn-eureka-service
mvn spring-boot:run
```
Wait until Eureka is fully started (check http://localhost:8761)

#### b. Start Core Service
```bash
cd hn-core-service
mvn spring-boot:run
```

#### c. Start Other Services
```bash
# Product Service
cd hn-product-service
mvn spring-boot:run

# Inventory Service
cd hn-inventory-service
mvn spring-boot:run

# Order Service
cd hn-order-service
mvn spring-boot:run
```

#### d. Start API Gateway
```bash
cd hn-gateway-service
mvn spring-boot:run
```

### 6. Verify Services

- **Eureka Dashboard:** http://localhost:8761
- **API Gateway:** http://localhost:8080
- **Core Service:** http://localhost:8081
- **Product Service:** http://localhost:8082
- **Inventory Service:** http://localhost:8083
- **Order Service:** http://localhost:8084

## ⚙️ Configuration

### Application Ports

| Service | Port | Database Port |
|---------|------|---------------|
| Eureka Server | 8761 | - |
| API Gateway | 8080 | - |
| Core Service | 8081 | 5432 |
| Product Service | 8082 | 5433 |
| Inventory Service | 8083 | 5435 |
| Order Service | 8084 | 5434 |

### JWT Configuration

Configure in `hn-core-service/application.yml`:

```yaml
jwt:
  signer-key: ${SECRET_KEY}
  jwt-expiration: 3600000      # 1 hour
  refresh-expiration: 2592000000  # 30 days
  issuer: hn-ecommerce
```

### Gateway Routes

API Gateway routes are configured with prefix `/api/v1`:

```yaml
/api/v1/core/**       → Core Service (8081)
/api/v1/product/**    → Product Service (8082)
/api/v1/inventory/**  → Inventory Service (8083)
/api/v1/order/**      → Order Service (8084)
```

## 📚 API Documentation

### Authentication Flow

1. **Register/Create User**
```bash
POST http://localhost:8080/api/v1/core/user
Content-Type: application/json

{
  "username": "user123",
  "password": "SecurePass123",
  "fullName": "John Doe",
  "dob": "01/01/1990"
}
```

2. **Login**
```bash
POST http://localhost:8080/api/v1/core/auth/token
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}
```

Response:
```json
{
  "code": 200,
  "message": "Operation successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "authenticated": true
  }
}
```

3. **Access Protected Endpoints**
```bash
GET http://localhost:8080/api/v1/core/user/myinfo
Authorization: Bearer {your-access-token}
```

4. **Refresh Token**
```bash
POST http://localhost:8080/api/v1/core/auth/refresh-token
Content-Type: application/json

{
  "refreshToken": "your-refresh-token"
}
```

5. **Logout**
```bash
POST http://localhost:8080/api/v1/core/auth/logout
Authorization: Bearer {your-access-token}
Content-Type: application/json

{
  "refreshToken": "your-refresh-token"
}
```

### Default Admin Account

```
Username: admin
Password: admin
Role: ADMIN (full system access)
```

## 🔐 Security

### Authentication & Authorization

- **JWT-based authentication** using Auth0 library
- **Access tokens** valid for 1 hour
- **Refresh tokens** valid for 30 days
- **Token invalidation** on logout (stored in database)
- **Role-based access control (RBAC)**
- **Permission-based authorization**

### Permission System

The system uses `@RequirePermission` annotation for automatic permission discovery:

```java
@GetMapping("/user/{id}")
@RequirePermission(name = "CoreUserDetail", description = "View user details")
@PreAuthorize("hasAuthority('CoreUserDetail')")
public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
    // ...
}
```

Permissions are automatically:
- Discovered at application startup
- Registered in the database
- Assigned to roles
- Enforced via Spring Security

### Security Features

- ✅ Password encryption using BCrypt
- ✅ JWT token validation on every request
- ✅ Token blacklist for logged-out tokens
- ✅ CORS configuration
- ✅ Protection against CSRF (stateless JWT)
- ✅ Request authentication via filters

## 🗄️ Database Schema

### Core Service Tables

**core_user**
- id (PK)
- username (unique)
- password (encrypted)
- full_name
- dob
- created_at, updated_at
- created_by, updated_by
- deleted (soft delete flag)

**core_role**
- name (PK)
- description
- created_at, updated_at
- deleted

**core_permission**
- name (PK)
- description
- created_at, updated_at
- deleted

**core_user_roles** (Join Table)
- user_id (FK)
- role_name (FK)

**core_role_permissions** (Join Table)
- role_name (FK)
- permission_name (FK)

**core_invalidatedtoken**
- id (JWT ID - jti claim)
- expiry_time

### Base Entity Pattern

All entities extend `BaseEntity` which provides:
```java
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
- createdBy: String
- updatedBy: String
- deleted: boolean
```

### Soft Delete Pattern

All entities use soft delete:
```java
user.softDelete();  // Sets deleted = true
user.restore();     // Sets deleted = false
```

## 🧪 Testing

### Manual Testing with cURL

**Login:**
```bash
curl -X POST http://localhost:8080/api/v1/core/auth/token \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

**Get Users (Protected):**
```bash
curl -X GET http://localhost:8080/api/v1/core/user \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## 🐳 Docker Support (Planned)

Docker Compose configuration will be added to easily spin up all services and databases.

## 📈 Future Enhancements

- [ ] Docker & Kubernetes deployment
- [ ] Message Queue (RabbitMQ/Kafka)
- [ ] Distributed tracing (Zipkin/Jaeger)
- [ ] Centralized logging (ELK Stack)
- [ ] Redis caching
- [ ] Payment gateway integration
- [ ] Email notifications
- [ ] File upload service
- [ ] Admin dashboard
- [ ] API rate limiting
- [ ] GraphQL support
- [ ] Swagger/OpenAPI documentation

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style Guidelines

- Follow Java naming conventions
- Use Lombok annotations appropriately
- Write meaningful commit messages
- Add JavaDoc for public methods
- Keep services loosely coupled

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- RYUK - Initial work

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Netflix OSS for Eureka
- Auth0 for JWT library
- All contributors and supporters

---

**Happy Coding! 🚀**
