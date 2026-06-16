# ProfitMint Login Module

A production-ready authentication module using **Java 21**, **Spring Boot 4.0.3**, and **MySQL** with cookie-based session management.

## Features

- User signup with email validation
- User login with BCrypt password verification
- Secure cookie-based session management
- HTTPOnly, Secure, SameSite=Strict cookies
- Remember Me functionality (30-day sessions)
- Session validation and cleanup
- Global exception handling

## Prerequisites

- Java 21
- Maven 3.8+
- MySQL 8.0+

## Database Setup

Create a MySQL database:

```sql
CREATE DATABASE profitmint;
```

## Configuration

Update `src/main/resources/application.properties` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/profitmint?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

```bash
# Using Maven
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080/api`

## API Endpoints

### 1. Signup

**Endpoint:** `POST /api/signup`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890",
  "password": "securePassword123",
  "referral": "REF001"
}
```

**Success Response (201 Created):**
```json
{
  "message": "User created successfully",
  "userId": 1
}
```

**Example curl:**
```bash
curl -X POST http://localhost:8080/api/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890",
    "password": "securePassword123",
    "referral": null
  }'
```

---

### 2. Login

**Endpoint:** `POST /api/login`

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "securePassword123",
  "rememberMe": true
}
```

**Success Response (200 OK):**
```json
{
  "message": "Login successful",
  "userId": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

**Response Headers:**
```
Set-Cookie: PROFITMINT_SESSION=<token>; HttpOnly; Secure; SameSite=Strict; Max-Age=2592000; Path=/
```

**Example curl:**
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -c cookies.txt \
  -d '{
    "email": "john@example.com",
    "password": "securePassword123",
    "rememberMe": true
  }'
```

---

### 3. Get Current User Profile

**Endpoint:** `GET /api/me`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890",
  "referral": "REF001",
  "createdAt": "2026-03-15T10:30:00"
}
```

**Example curl:**
```bash
curl -X GET http://localhost:8080/api/me \
  -b cookies.txt
```

---

### 4. Validate Session

**Endpoint:** `GET /api/validate-session`

**Success Response (200 OK):**
```json
{
  "valid": true,
  "message": "Session is valid"
}
```

**Example curl:**
```bash
curl -X GET http://localhost:8080/api/validate-session \
  -b cookies.txt
```

---

### 5. Logout

**Endpoint:** `POST /api/logout`

**Success Response (200 OK):**
```json
{
  "message": "Logged out successfully"
}
```

**Example curl:**
```bash
curl -X POST http://localhost:8080/api/logout \
  -b cookies.txt \
  -c cookies.txt
```

---

### 6. Logout from All Devices

**Endpoint:** `POST /api/logout-all`

**Success Response (200 OK):**
```json
{
  "message": "Logged out from all devices successfully"
}
```

**Example curl:**
```bash
curl -X POST http://localhost:8080/api/logout-all \
  -b cookies.txt \
  -c cookies.txt
```

---

## Error Responses

### Validation Error (400 Bad Request)
```json
{
  "timestamp": "2026-03-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/signup",
  "validationErrors": {
    "email": "Please provide a valid email address",
    "password": "Password must be between 8 and 100 characters"
  }
}
```

### Email Already Exists (409 Conflict)
```json
{
  "timestamp": "2026-03-15T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "User with email 'john@example.com' already exists",
  "path": "/api/signup"
}
```

### Invalid Credentials (401 Unauthorized)
```json
{
  "timestamp": "2026-03-15T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid email or password",
  "path": "/api/login"
}
```

### Invalid Session (401 Unauthorized)
```json
{
  "timestamp": "2026-03-15T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Session is invalid or expired",
  "path": "/api/me"
}
```

---

## Project Structure

```
src/main/java/com/ProfitMint/
├── ProfitMintApplication.java
└── login/
    ├── config/
    │   ├── SecurityConfig.java
    │   └── WebConfig.java
    ├── controller/
    │   └── AuthController.java
    ├── dto/
    │   ├── ApiErrorResponse.java
    │   ├── LoginRequest.java
    │   ├── LoginResponse.java
    │   ├── SignupRequest.java
    │   ├── SignupResponse.java
    │   └── UserProfileResponse.java
    ├── entity/
    │   ├── User.java
    │   └── UserSession.java
    ├── exception/
    │   ├── EmailAlreadyExistsException.java
    │   ├── GlobalExceptionHandler.java
    │   ├── InvalidCredentialsException.java
    │   └── InvalidSessionException.java
    ├── repository/
    │   ├── UserRepository.java
    │   └── UserSessionRepository.java
    ├── service/
    │   └── AuthService.java
    └── util/
        ├── CookieUtil.java
        ├── PasswordUtil.java
        └── SessionTokenGenerator.java
```

## Database Schema

### users table
| Column     | Type         | Constraints       |
|------------|--------------|-------------------|
| id         | BIGINT       | PRIMARY KEY, AUTO |
| name       | VARCHAR(100) | NOT NULL          |
| email      | VARCHAR(255) | UNIQUE, NOT NULL  |
| phone      | VARCHAR(20)  |                   |
| password   | VARCHAR(255) | NOT NULL          |
| referral   | VARCHAR(50)  |                   |
| created_at | DATETIME     | NOT NULL          |
| updated_at | DATETIME     | NOT NULL          |

### user_sessions table
| Column      | Type         | Constraints       |
|-------------|--------------|-------------------|
| id          | BIGINT       | PRIMARY KEY, AUTO |
| token       | VARCHAR(255) | UNIQUE, NOT NULL  |
| user_id     | BIGINT       | FK -> users.id    |
| expires_at  | DATETIME     | NOT NULL          |
| remember_me | BOOLEAN      | NOT NULL          |
| created_at  | DATETIME     | NOT NULL          |
| ip_address  | VARCHAR(45)  |                   |
| user_agent  | VARCHAR(500) |                   |

## Security Features

- **BCrypt Password Hashing:** Strength 12 for secure password storage
- **Secure Session Tokens:** 256-bit cryptographically secure random tokens
- **HTTPOnly Cookies:** Prevents XSS attacks from accessing session tokens
- **Secure Flag:** Ensures cookies are only sent over HTTPS
- **SameSite=Strict:** Prevents CSRF attacks
- **Session Cleanup:** Automatic cleanup of expired sessions every hour

