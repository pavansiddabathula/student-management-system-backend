
# 📘 Student Management System – Spring Boot RESTful Microservice

### 🔍 Overview  
The **Student Management System** is a backend RESTful microservice built using **Spring Boot**, designed to manage student details efficiently.  
It supports **secure registration, update, retrieval, and deletion** of student records while maintaining clean architecture, validations, centralized exception handling, and proper success/error responses.

This project is built following **industry-level coding standards** — modular design, DTO pattern, proper logging, and future-ready structure (JWT/OAuth2 integration planned).

---

### 🧱 Architecture & Design

#### ⚙️ Architecture Layers
```
Controller  →  Service  →  Repository  →  Database
```

- **Controller Layer** — Handles HTTP requests and responses.  
- **Service Layer** — Contains business logic and validation checks.  
- **Repository Layer** — Interacts with the MySQL database using Spring Data JPA.  
- **DTOs (Data Transfer Objects)** — Used for clean separation between API models and database entities.  
- **Global Exception Handler** — Manages validation and application errors in a centralized way.  

#### 🧩 Design Practices
- **Loose coupling** via interfaces and dependency injection.  
- **Separation of concerns** using DTOs and Mappers.  
- **Constants** for error messages and response codes (no hardcoding).  
- **Logging** with `Slf4j` for clear debugging and traceability.  
- **Scalable microservice-ready structure** — easy to extend for JWT or OAuth2 security.

---

### 🚀 Core Functionalities

#### 1️⃣ Register Student (POST `/api/students/register`)
- Accepts student details as JSON (`StudentRequestDTO`).
- Performs:
  - Field-level validation (`@NotBlank`, `@Email`, `@Pattern`).
  - Business validations (duplicate username/email).
  - Password encryption using `BCryptPasswordEncoder`.
- On success → returns structured success response (`StudentResponseDTO`).
- On failure → returns custom error response with code, message, and timestamp.

#### 2️⃣ Get All Students (GET `/api/students`)
- Returns all student records in a clean, formatted list.  
- Converts entity to response DTO (no sensitive data like passwords).

#### 3️⃣ Get Student by Username (GET `/api/students/{username}`)
- Fetches a single student using username.  
- If not found → returns standardized error response with message.

#### 4️⃣ Update Student (PUT `/api/students/{rollNumber}`)
- Updates existing student info (like email, phone, branch).  
- Performs both field validation and business validation.  
- Returns updated response DTO on success.

#### 5️⃣ Delete Student (DELETE `/api/students/{rollNumber}`)
- Deletes student by roll number.  
- Validates before deletion (if student doesn’t exist → custom error).  
- Returns confirmation message in success response.

---

### 🧰 Tech Stack

| Layer | Technology |
|-------|-------------|
| Backend | **Spring Boot (v3.x)** |
| ORM | **Spring Data JPA** |
| Database | **MySQL** |
| Validation | **Jakarta Validation API** |
| Password Security | **BCryptPasswordEncoder** |
| Exception Handling | **@ControllerAdvice**, Custom ErrorResponse |
| Logging | **Slf4j (Lombok)** |
| Testing | **Postman (API Testing)** |
| Build Tool | **Maven** |
| Version Control | **Git & SourceTree (Bitbucket/GitHub)** |

---

### 🧩 Engineering Practices

✅ Proper project structure with packages like:  
```
com.techcode.studentmgmt.controller  
com.techcode.studentmgmt.service  
com.techcode.studentmgmt.repository  
com.techcode.studentmgmt.entity  
com.techcode.studentmgmt.dto.requestdto  
com.techcode.studentmgmt.dto.responsedto  
com.techcode.studentmgmt.exception  
com.techcode.studentmgmt.constants  
com.techcode.studentmgmt.modelmappers  
```

✅ **Centralized Exception Handling** → clean API errors  
✅ **Meaningful HTTP status codes** (200, 201, 400, 404, 500)  
✅ **Clean code** using OOP principles  
✅ **Unit testing-ready design**  
✅ **Industry-level commit messages** and version control practices  

---

### 🧠 Key Highlights

- Implemented **end-to-end REST API** with robust validation.
- Added **Global Exception Handler** for all runtime and validation errors.
- Used **DTOs + Mapper classes** to keep data flow clean and secure.
- Added **logging** for all critical points — service calls, validation, and DB operations.
- Properly handled **success and failure responses** using a unified structure.
- Tested APIs using **Postman** with clear request/response formats.

---

### 🔒 Security & Future Enhancements

- Integrate **Spring Security + JWT Authentication** for login and token-based authorization.  
- Add **OAuth 2.0** support for social login (Google/GitHub).  
- Extend the system to a **microservice ecosystem** (User-Service, Student-Service, Auth-Service).  
- Deploy on **AWS EC2**, host DB on **AWS RDS**, and integrate **Redis Cache** for performance.  

---

### 🧪 Sample Request (POST /api/students/register)

**Request Body:**
```json
{
  "rollNumber": "21CSE04582",
  "firstName": "Anjali",
  "lastName": "Reddy",
  "email": "anjali.reddy@example.com",
  "branch": "CSE",
  "username": "anjali_r",
  "password": "Ecap@123",
  "confirmPassword": "Ecap@123",
  "phoneNumber": "9876501234"
}
```

**Success Response:**
```json
{
  "status": "SUCCESS",
  "message": "Student registered successfully",
  "data": {
    "rollNumber": "21CSE04582",
    "username": "anjali_r",
    "branch": "CSE"
  },
  "timestamp": "2025-11-12T10:15:30"
}
```

**Failure Response:**
```json
{
  "status": "FAILURE",
  "errorCode": "STUDENT_ALREADY_EXISTS",
  "message": "Username 'anjali_r' is already taken.",
  "timestamp": "2025-11-12T10:15:35"
}
```

---

### 🧾 How to Run

1. Clone the repository  
   ```bash
   git clone https://github.com/pavansiddabathula/student-management-system-spring-jpa
   ```

2. Configure `application.properties`
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/studentdb
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

3. Run the project  
   ```bash
   mvn spring-boot:run
   ```

4. Test APIs using Postman — Base URL:  
   ```
   http://localhost:8080/api/students
   ```

---

### 📈 Results & Learnings

- Learned **real-world backend project structuring**.  
- Built **production-grade REST APIs** with **validation & exception handling**.  
- Gained **debugging, logging, and version control** experience.  
- Practiced **clean, modular, and scalable code design**.  

---

**GitHub Repo:** [pavansiddabathula/student-management-system-spring-jpa](https://github.com/pavansiddabathula/student-management-system-spring-jpa)
