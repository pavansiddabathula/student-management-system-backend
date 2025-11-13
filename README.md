
Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   # 📘 Student Management System – Spring Boot RESTful Microservice  ### 🔍 Overview    The **Student Management System** is a backend RESTful microservice built using **Spring Boot**, designed to manage student details efficiently.    It supports **secure registration, update, retrieval, and deletion** of student records while maintaining clean architecture, validations, centralized exception handling, and proper success/error responses.  This project follows **industry-level backend standards** — modular design, DTO patterns, logging, and readiness for future extensions like **Spring Security with JWT and OAuth2.0**.  ---  ## 🧱 Architecture & Design  ### ⚙️ Architecture Layers   `

Controller → Service → Repository → Database

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   - **Controller Layer** — Handles HTTP requests and responses.    - **Service Layer** — Contains business logic and validation checks.    - **Repository Layer** — Interacts with MySQL using Spring Data JPA.    - **DTOs (Data Transfer Objects)** — Ensures clean separation between API and DB models.    - **Global Exception Handler** — Manages validation and runtime errors centrally.    ### 🧩 Design Practices  - Loose coupling via **interfaces** and **dependency injection**.    - DTOs + **Mapper classes** for entity-object conversion.    - Constants for **error codes, success messages, and validation keys**.    - **Slf4j logging** for better debugging and observability.    - Microservice-ready modular structure (JWT/OAuth2 can be added easily).    ---  ## 🚀 Core Functionalities  | Feature | Description |  |----------|--------------|  | **Register Student** | Creates new student record with validation and encrypted password |  | **Fetch All Students** | Retrieves all student data |  | **Fetch by Username / Roll Number** | Get student details by username or roll number |  | **Update Student** | Updates existing record by roll number |  | **Delete Student** | Removes student record from database |  | **Validation + Exception Handling** | Field + Business-level validation with user-friendly messages |  ---  ## 🧰 Tech Stack  | Layer | Technology |  |-------|-------------|  | Backend | **Spring Boot (v3.x)** |  | ORM | **Spring Data JPA** |  | Database | **MySQL** |  | Validation | **Jakarta Validation API** |  | Security | **BCryptPasswordEncoder** |  | Logging | **Slf4j (Lombok)** |  | Testing | **Postman (API Testing)** |  | Build Tool | **Maven** |  | Version Control | **Git, SourceTree, GitHub** |  ---  ## 📁 Package Structure   `

com.techcode.studentmgmt│├── controller├── service├── repository├── entity├── dto│ ├── requestdto│ └── responsedto├── exception├── constants├── modelmappers└── utils

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`---  ## 🧩 API Documentation  Base URL:`  

[http://localhost:8080/api/students](http://localhost:8080/api/students)

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML``   ---  ### 1️⃣ **Register Student**  **URL:** `POST /api/students/register`    **Description:** Registers a new student with validations.  #### 📨 Request Body  ```json  {    "rollNumber": "21CSE04582",    "firstName": "Anjali",    "lastName": "Reddy",    "email": "anjali.reddy@example.com",    "branch": "CSE",    "username": "anjali_r",    "password": "Ecap@123",    "confirmPassword": "Ecap@123",    "phoneNumber": "9876501234"  }   ``

#### ✅ Success Response

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "status": "SUCCESS",    "message": "Student registered successfully",    "data": {      "rollNumber": "21CSE04582",      "username": "anjali_r",      "branch": "CSE"    },    "timestamp": "2025-11-12T18:10:22"  }   `

#### ❌ Failure Response (Duplicate Username)

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "status": "FAILURE",    "errorCode": "STUDENT_ALREADY_EXISTS",    "message": "Username 'anjali_r' is already taken.",    "timestamp": "2025-11-12T18:12:45"  }   `

### 2️⃣ **Get All Students**

**URL:** GET /api/students**Description:** Fetches all registered students.

#### ✅ Success Response

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "status": "SUCCESS",    "message": "Students fetched successfully",    "data": [      {        "rollNumber": "21CSE04582",        "firstName": "Anjali",        "lastName": "Reddy",        "email": "anjali.reddy@example.com",        "branch": "CSE",        "username": "anjali_r",        "phoneNumber": "9876501234"      },      {        "rollNumber": "21EE045911",        "firstName": "Sneha",        "lastName": "Kumar",        "email": "sneha.kumaar@example.com",        "branch": "EE",        "username": "sneha_ka",        "phoneNumber": "9876501236"      }    ],    "timestamp": "2025-11-12T18:15:42"  }   `

### 3️⃣ **Get Student by Username**

**URL:** GET /api/students/username/{username}**Example:** GET /api/students/username/sneha\_ka

#### ✅ Success Response

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "status": "SUCCESS",    "message": "Student fetched successfully",    "data": {      "rollNumber": "21EE045911",      "firstName": "Sneha",      "lastName": "Kumar",      "email": "sneha.kumaar@example.com",      "branch": "EE",      "username": "sneha_ka",      "phoneNumber": "9876501236"    },    "timestamp": "2025-11-12T18:18:30"  }   `

#### ❌ Failure Response

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "status": "FAILURE",    "errorCode": "STUDENT_NOT_FOUND",    "message": "Student with username 'sneha_ka' not found",    "timestamp": "2025-11-12T18:19:02"  }   `

### 4️⃣ **Get Student by Roll Number**

**URL:** GET /api/students/roll/{rollNumber}**Example:** GET /api/students/roll/21CSE04582

#### ✅ Success Response

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "status": "SUCCESS",    "message": "Student fetched successfully",    "data": {      "rollNumber": "21CSE04582",      "firstName": "Anjali",      "lastName": "Reddy",      "email": "anjali.reddy@example.com",      "branch": "CSE",      "username": "anjali_r",      "phoneNumber": "9876501234"    },    "timestamp": "2025-11-12T18:20:50"  }   `

#### ❌ Failure Response

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "status": "FAILURE",    "errorCode": "STUDENT_NOT_FOUND",    "message": "Student with roll number '21CSE04582' not found",    "timestamp": "2025-11-12T18:21:10"  }   `

### 5️⃣ **Update Student**

**URL:** PUT /api/students/{rollNumber}**Example:** PUT /api/students/21CSE04582

#### 📨 Request Body

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "firstName": "Anjali",    "lastName": "Reddy",    "email": "anjali.updated@example.com",    "branch": "IT",    "username": "anjali_r",    "phoneNumber": "9876509999",    "password": "NewPass@123",    "confirmPassword": "NewPass@123"  }   `

#### ✅ Success Response

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "status": "SUCCESS",    "message": "Student updated successfully",    "data": {      "rollNumber": "21CSE04582",      "firstName": "Anjali",      "lastName": "Reddy",      "email": "anjali.updated@example.com",      "branch": "IT",      "username": "anjali_r",      "phoneNumber": "9876509999"    },    "timestamp": "2025-11-12T18:25:15"  }   `

#### ❌ Failure Response

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "status": "FAILURE",    "errorCode": "STUDENT_NOT_FOUND",    "message": "Student with roll number '21CSE04582' not found",    "timestamp": "2025-11-12T18:26:00"  }   `

### 6️⃣ **Delete Student**

**URL:** DELETE /api/students/{rollNumber}**Example:** DELETE /api/students/21CSE04582

#### ✅ Success Response

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "status": "SUCCESS",    "message": "Student deleted successfully",    "timestamp": "2025-11-12T18:30:10"  }   `

#### ❌ Failure Response

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   {    "status": "FAILURE",    "errorCode": "STUDENT_NOT_FOUND",    "message": "Student with roll number '21CSE04582' not found",    "timestamp": "2025-11-12T18:31:05"  }   `

🧾 How to Run
-------------

1.  Clone the repository
    

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   git clone https://github.com/pavansiddabathula/student-management-system-spring-jpa   `

1.  Configure application.properties
    

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   spring.datasource.url=jdbc:mysql://localhost:3306/studentdb  spring.datasource.username=root  spring.datasource.password=yourpassword  spring.jpa.hibernate.ddl-auto=update  spring.jpa.show-sql=true   `

1.  Run the project
    

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   mvn spring-boot:run   `

1.  Test APIs using Postman — Base URL:
    

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML`   http://localhost:8080/api/students   `

### 📈 Results & Learnings

*   Built **production-grade REST APIs** with validation & exception handling.
    
*   Learned **real-world backend project structuring**.
    
*   Practiced **logging, debugging, and version control**.
    
*   Designed **modular, scalable, and maintainable code** ready for security integration (JWT/OAuth2).
    

**GitHub Repo:** [https://github.com/pavansiddabathula/student-management-system-spring-jpa](https://github.com/pavansiddabathula/student-management-system-spring-jpa)

Plain textANTLR4BashCC#CSSCoffeeScriptCMakeDartDjangoDockerEJSErlangGitGoGraphQLGroovyHTMLJavaJavaScriptJSONJSXKotlinLaTeXLessLuaMakefileMarkdownMATLABMarkupObjective-CPerlPHPPowerShell.propertiesProtocol BuffersPythonRRubySass (Sass)Sass (Scss)SchemeSQLShellSwiftSVGTSXTypeScriptWebAssemblyYAMLXML``   ---  If you want, I can **also include a section showing the actual `SuccessResponse` and `ErrorResponse` classes** along with **how to use a generic response builder utility** — this will make your README fully professional and ready to showcase on LinkedIn.    Do you want me to add that?   ``
