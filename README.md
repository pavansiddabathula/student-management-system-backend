📘 Student Management System – Spring Boot RESTful Microservice (Advanced & Secure)
===================================================================================

### 🔍 Overview

The **Student Management System** is a secure, production-ready backend RESTful microservice built using **Spring Boot**.Along with traditional CRUD operations, this enhanced version includes:

*   **Admin & Student Authentication**
    
*   **JWT-based Authorization**
    
*   **Role-based Access Control**
    
*   **OTP-based Forgot Password Flow**
    
*   **Email Notifications**
    
*   **Industry-level validation & error handling**
    

Admins can manage both **students and admins**, while students can access **only their own account**.

This project demonstrates **real-world, industry-standard backend engineering**.

🧱 Architecture & Design
========================

### ⚙️ Architecture Layers

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   Controller  →  Service  →  Security  →  Repository  →  Database   `

### 🧩 Key Components

*   **Controller Layer** — Handles REST endpoints.
    
*   **Service Layer** — Contains business & validation logic.
    
*   **Security Layer** — JWT filters, authentication, role-based authorization.
    
*   **Repository Layer** — Database communication (Spring Data JPA).
    
*   **DTOs** — Clean data transfer objects for request & response.
    
*   **Global Exception Handler** — Centralized error responses.
    

### 🧩 Design Principles

*   Loose coupling with interfaces
    
*   DTOs + Mappers to prevent exposing entities
    
*   Reusable constants for error codes/messages
    
*   Clean and meaningful log messages
    
*   High-level modular structure
    
*   Ready for microservice scaling
    

🔐 Security (NEW FEATURE)
=========================

### ✔ JWT Authentication

Both Admin and Student login return a **JWT token**.

Token contains:

*   User identifier (adminid / rollNumber)
    
*   Roles (ADMIN / STUDENT)
    
*   Issued & expiry time
    

### ✔ Role-Based Authorization

RolePermissions**ADMIN**Manage both admins and students**STUDENT**Access only own profile, password reset, update own info

Protected endpoints require:

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`Authorization: Bearer` 

✉️ Email Features (NEW)
=======================

Automatically sends emails for:

*   Account creation (student/admin)
    
*   Credentials (username + temporary password)
    
*   OTP for forgot password
    
*   Password reset confirmation
    

All emails use industry-level templates.

🔁 OTP-Based Password Reset (NEW)
=================================

### Step 1: Request OTP

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   POST /api/auth/forgot-password   `

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "identifier" : "21481A05K6",    "email":"student@gmail.com"  }   `

### Step 2: Verify OTP

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   POST /api/auth/verify-otp   `

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "identifier": "21481A05K6",    "otp" : "417024"  }   `

### Step 3: Set New Password

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   POST /api/auth/set-password   `

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "identifier" : "21481A05K6",    "newPassword":"Password@1234",    "confirmPassword":"Password@1234"  }   `

🚀 Core Functionalities (Updated)
=================================

1️⃣ Admin Login
---------------

**POST /api/auth/admin/login**

Test Payloads:

Empty password → should fail

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   { "adminid" : "ADM00", "password": "" }   `

Wrong password → fail

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   { "adminid" : "ADM001", "password": "7x5mjkgljgjlh" }   `

Correct

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   { "adminid" : "ADM001", "password": "7x5mxJRNO*" }   `

Returns JWT Token:

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   eyJhbGciOiJIUzI1NiJ9...   `

2️⃣ Student Login
-----------------

**POST /api/auth/student/login**

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "rollNumber": "21481A05K6",    "password" : "Password@1234"  }   `

3️⃣ Create Student (ADMIN)
--------------------------

**POST /api/students/create**

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "rollNumber": "21481A05K6",    "fullName": "Siddabathula Pavan Kumar",    "email": "jayaramsiddabathula37@gmail.com",    "phoneNumber": "9988776655",    "branch": "CSE"  }   `

On success → email sent with credentials.

4️⃣ Get All Students (ADMIN)
----------------------------

**GET /api/students/all**

5️⃣ Get Student by Username (ADMIN)
-----------------------------------

**GET /api/students/username/{fullName}**

6️⃣ Update Student (ADMIN)
--------------------------

**PUT /api/students/update/21481A05K6**

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "rollNumber": "21481A05K6",    "fullName" : "Siddabathula Pavan Kumar",    "email": "pavansiddabathula@gmail.com",    "phoneNumber": "9988776655",    "branch": "CSE"  }   `

7️⃣ Student Profile (STUDENT)
-----------------------------

**GET /api/students/profile**

JWT required.

8️⃣ Reset Password (Logged-in Student)
--------------------------------------

**PUT /api/students/reset-password**

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "oldPassword" : "oDms**owph",    "newPassword" : "ResetPassword@1234",    "confirmPassword":"ResetPassword@1234"  }   `

🧪 Validation Testing (NEW)
===========================

### ❌ Failure Case – Empty Fields

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "rollNumber": "",    "fullName": "",    "email": "",    "phoneNumber": "",    "branch": ""  }   `

### ❌ Failure Case – Wrong formats

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "rollNumber": "21481A05K2",    "fullName": "",    "email": "pavansiddabathula@gmail.com",    "phoneNumber": "998877665",    "branch": " "  }   `

### ✔ Success Case

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "rollNumber": "21481A05K6",    "fullName": "Siddabathula Pavan Kumar",    "email": "jayaramsiddabathula37@gmail.com",    "phoneNumber": "9988776655",    "branch": "CSE"  }   `

🧰 Tech Stack
=============

LayerTechnologyBackend**Spring Boot 3**Security**Spring Security + JWT**EmailJavaMailSenderORMSpring Data JPADatabaseMySQLCache (optional)RedisValidationJakarta ValidationLoggingSlf4jBuild ToolMaven

🧩 Engineering Practices
========================

*   Clean, modular package structure
    
*   DTOs + Mappers
    
*   Strong field & business validations
    
*   Meaningful error messages
    
*   Centralized response format
    
*   Global exception handling
    
*   Layered architecture
    
*   Consistent logging
    
*   Industry-level naming conventions
    

📈 How to Run
=============

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   git clone https://github.com/pavansiddabathula/student-management-system-spring-jpa   `

Configure application.properties

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   spring.datasource.url=jdbc:mysql://localhost:3306/studentdb  spring.datasource.username=root  spring.datasource.password=yourpassword  spring.jpa.hibernate.ddl-auto=update  spring.jpa.show-sql=true   `

Run:

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   mvn spring-boot:run   `

Test APIs using Postman.

📊 Results & Learnings
======================

*   Implemented complete **authentication & authorization system**
    
*   Built **secure user flows** using JWT
    
*   Added **email + OTP-based recovery system**
    
*   Learned clean backend design & enterprise validation
    
*   Improved debugging & logging skills
    
*   Gained experience with real-world user management
    

🔗 GitHub Repository
====================

👉 [https://github.com/pavansiddabathula/student-management-system-spring-jpa](https://github.com/pavansiddabathula/student-management-system-spring-jpa)
