# 🏦 MAVERICK BANK BACKEND - COMPLETE FRONTEND DEVELOPMENT REFERENCE

> **This comprehensive guide contains EVERYTHING you need to build the frontend for Maverick Bank**

---

## 📋 TABLE OF CONTENTS
1. [Quick Start](#quick-start)
2. [Backend Architecture Overview](#backend-architecture)
3. [Database Schema & Entities](#database-schema)
4. [Authentication & Security](#authentication)
5. [Complete API Reference](#api-reference)
6. [Business Rules & Workflows](#business-rules)
7. [Error Handling](#error-handling)
8. [Sample API Requests/Responses](#sample-requests)
9. [Frontend Integration Guidelines](#frontend-integration)
10. [Testing & Validation](#testing)

---

## 🚀 QUICK START

### Backend Status
- ✅ **Running on:** http://localhost:9090
- ✅ **Swagger UI:** http://localhost:9090/swagger-ui.html
- ✅ **Technology:** Spring Boot + MySQL + JWT
- ✅ **CORS Enabled:** Supports localhost:4200 (Angular default)

### Test Credentials
```json
{
  "admin": { "username": "admin", "password": "admin123", "role": "ADMIN" },
  "employee": { "username": "employee", "password": "employee123", "role": "EMPLOYEE" }
}
```

### Authentication Flow
1. **Login:** `POST /api/auth/login` → Get JWT token
2. **Use Token:** Add `Authorization: Bearer <token>` to all requests
3. **Token Format:** `eyJhbGciOiJIUzUxMiJ9...` (valid for 24 hours)

---

## 🏗️ BACKEND ARCHITECTURE

### Layered Architecture
```
Frontend (Angular) 
    ↓ HTTP/REST
Controllers (REST APIs)
    ↓ Business Logic
Services (Business Layer)
    ↓ Data Access
Repositories (JPA/Hibernate)
    ↓ Database
MySQL Database
```

### Key Technologies
- **Framework:** Spring Boot 2.7.0
- **Security:** Spring Security + JWT
- **Database:** MySQL 8.x with JPA/Hibernate
- **Documentation:** Swagger/OpenAPI 3
- **Build:** Maven
- **Testing:** JUnit, Spring Boot Test

### Project Structure
```
src/main/java/com/maverickbank/
├── controller/          # REST API endpoints
├── service/            # Business logic layer
├── repository/         # Data access layer
├── entity/            # JPA entities (database models)
├── security/          # JWT & authentication
├── config/            # Configuration classes
└── DataInitializer.java # Seed data
```

---

## 🗄️ DATABASE SCHEMA & ENTITIES

### Core Entities

#### 1. User Entity
```java
@Entity
@Table(name = "users")
public class User {
    private Long id;                    // Primary key
    private String username;            // Unique login username
    private String password;            // Encrypted password
    private String name;               // Full name
    private String email;              // Email address
    private String contactNumber;      // Phone number
    private String address;            // Physical address
    private LocalDate dateOfBirth;     // Date of birth
    private String aadharNumber;       // Aadhar number (KYC)
    private String panNumber;          // PAN number (KYC)
    private Integer age;               // Calculated age
    private String gender;             // MALE/FEMALE/OTHER
    private Boolean isActive;          // Account status
    private LocalDateTime lastLogin;   // Last login timestamp
    private String profilePicture;     // Profile image URL
    private Set<Role> roles;           // User roles (CUSTOMER/EMPLOYEE/ADMIN)
}
```

#### 2. Account Entity
```java
@Entity
public class Account {
    private Long id;                   // Primary key
    private String accountNumber;      // Unique account number
    private String accountType;        // SAVINGS/CURRENT/FIXED_DEPOSIT
    private String ifscCode;           // Bank IFSC code
    private String branchName;         // Branch name
    private String branchAddress;      // Branch address
    private BigDecimal balance;        // Current balance
    private User user;                 // Account owner (Many-to-One)
    private Set<Transaction> transactions; // Account transactions
}
```

#### 3. Transaction Entity
```java
@Entity
public class Transaction {
    private Long id;                   // Primary key
    private String type;               // DEPOSIT/WITHDRAW/TRANSFER
    private BigDecimal amount;         // Transaction amount
    private LocalDateTime date;        // Transaction timestamp
    private String description;        // Transaction description
    private Account account;           // Source account (Many-to-One)
    private String toAccountNumber;    // Destination account (for transfers)
    private String status;             // PENDING/COMPLETED/FAILED
}
```

#### 4. Loan Entity
```java
@Entity
public class Loan {
    private Long id;                   // Primary key
    private BigDecimal amount;         // Loan amount
    private BigDecimal interestRate;   // Interest rate
    private Integer tenureMonths;      // Tenure in months
    private String status;             // APPLIED/APPROVED/REJECTED/DISBURSED
    private LocalDate applicationDate; // Application date
    private String purpose;            // Loan purpose
    private String employeeComments;   // Employee remarks
    private Long approvedBy;           // Approved by employee ID
    private User user;                 // Loan applicant (Many-to-One)
    private LoanType loanType;         // Loan type (Many-to-One)
}
```

#### 5. LoanType Entity
```java
@Entity
public class LoanType {
    private Long id;                   // Primary key
    private String typeName;           // HOME/CAR/PERSONAL/EDUCATION/BUSINESS
    private String description;        // Loan type description
    private BigDecimal interestRate;   // Default interest rate
    private BigDecimal maxAmount;      // Maximum loan amount
    private Integer maxTenure;         // Maximum tenure (months)
    private Boolean isActive;          // Active status
}
```

#### 6. Beneficiary Entity
```java
@Entity
public class Beneficiary {
    private Long id;                   // Primary key
    private String beneficiaryName;    // Beneficiary name
    private String accountNumber;      // Beneficiary account number
    private String ifscCode;           // Beneficiary bank IFSC
    private String bankName;           // Beneficiary bank name
    private User user;                 // Beneficiary owner (Many-to-One)
}
```

#### 7. AccountRequest Entity
```java
@Entity
public class AccountRequest {
    private Long id;                   // Primary key
    private Long userId;               // Requesting user ID
    private String accountType;        // Requested account type
    private String ifscCode;           // Preferred IFSC code
    private String branchName;         // Preferred branch
    private String branchAddress;      // Preferred branch address
    private String status;             // PENDING/APPROVED/REJECTED
    private LocalDateTime requestDate; // Request timestamp
    private String employeeComments;   // Employee remarks
    private Long processedBy;          // Processed by employee ID
}
```

#### 8. Bank & Branch Entities
```java
@Entity
public class Bank {
    private Long id;                   // Primary key
    private String bankName;           // Bank name
    private String bankCode;           // Bank code
    private Set<Branch> branches;      // Bank branches
}

@Entity
public class Branch {
    private Long id;                   // Primary key
    private String branchName;         // Branch name
    private String branchCode;         // Branch code
    private String ifscCode;           // IFSC code
    private String address;            // Branch address
    private String contactNumber;      // Branch contact
    private Bank bank;                 // Parent bank (Many-to-One)
}
```

#### 9. Role Entity
```java
@Entity
public class Role {
    private Long id;                   // Primary key
    private String name;               // CUSTOMER/EMPLOYEE/ADMIN
}
```

### Entity Relationships
```
User (1) ←→ (M) Account
User (1) ←→ (M) Loan
User (1) ←→ (M) Beneficiary
User (1) ←→ (M) AccountRequest
User (M) ←→ (M) Role
Account (1) ←→ (M) Transaction
Loan (M) ←→ (1) LoanType
Bank (1) ←→ (M) Branch
```

---

## 🔐 AUTHENTICATION & SECURITY

### JWT Authentication Flow
1. **Login Request:**
   ```http
   POST /api/auth/login
   Content-Type: application/json
   
   {
     "username": "admin",
     "password": "admin123"
   }
   ```

2. **Login Response:**
   ```json
   {
     "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3..."
   }
   ```

3. **Authenticated Requests:**
   ```http
   Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3...
   ```

### Role-Based Access Control
| Role | Permissions |
|------|------------|
| **ADMIN** | Full system access, user management, all reports |
| **EMPLOYEE** | Account approval, loan processing, customer support |
| **CUSTOMER** | Own accounts, transactions, loans, beneficiaries |

### Security Configuration
- **Token Expiry:** 24 hours (86400000 ms)
- **Password Encryption:** BCrypt
- **CORS:** Enabled for localhost:4200
- **Protected Endpoints:** All except `/api/auth/login` and `/api/auth/register`

---

## 📡 COMPLETE API REFERENCE

### 🔑 Authentication APIs

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "string",
  "password": "string"
}

Response: { "token": "jwt_token_string" }
```

#### Register Customer
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "string",
  "password": "string",
  "name": "string",
  "email": "string",
  "contactNumber": "string",
  "address": "string",
  "dateOfBirth": "2000-01-15",
  "aadharNumber": "123456789012",
  "panNumber": "ABCDE1234F",
  "gender": "MALE|FEMALE|OTHER"
}

Response: { "message": "User registered successfully", "userId": 123 }
```

#### Register Employee (Admin Only)
```http
POST /api/auth/register-employee
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "username": "string",
  "password": "string",
  "name": "string",
  "email": "string",
  "contactNumber": "string",
  "address": "string"
}

Response: { "message": "Employee registered successfully", "userId": 123 }
```

#### Change Password
```http
POST /api/auth/change-password
Authorization: Bearer <token>
Content-Type: application/json

{
  "currentPassword": "string",
  "newPassword": "string"
}

Response: { "message": "Password changed successfully" }
```

### 👤 User Management APIs

#### Get User Profile
```http
GET /api/users/profile
Authorization: Bearer <token>

Response: {
  "id": 1,
  "username": "string",
  "name": "string",
  "email": "string",
  "contactNumber": "string",
  "address": "string",
  "dateOfBirth": "2000-01-15",
  "aadharNumber": "123456789012",
  "panNumber": "ABCDE1234F",
  "gender": "MALE",
  "age": 25,
  "isActive": true,
  "lastLogin": "2025-07-06T10:30:00",
  "roles": [{"id": 1, "name": "CUSTOMER"}]
}
```

#### Update User Profile
```http
PUT /api/users/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "string",
  "email": "string",
  "contactNumber": "string",
  "address": "string",
  "profilePicture": "string"
}

Response: { "message": "Profile updated successfully" }
```

#### Get All Users (Admin Only)
```http
GET /api/users
Authorization: Bearer <admin_token>

Response: [
  {
    "id": 1,
    "username": "string",
    "name": "string",
    "email": "string",
    "roles": [{"name": "CUSTOMER"}],
    "isActive": true
  }
]
```

### 💳 Account Management APIs

#### Get Accounts by User
```http
GET /api/accounts/user/{userId}
Authorization: Bearer <token>

Response: [
  {
    "id": 1,
    "accountNumber": "ACC001234567890",
    "accountType": "SAVINGS",
    "ifscCode": "MVB001001",
    "branchName": "Main Branch",
    "branchAddress": "123 Main Street",
    "balance": 50000.00,
    "user": { "id": 1, "name": "John Doe" }
  }
]
```

#### Get Account by Number
```http
GET /api/accounts/{accountNumber}
Authorization: Bearer <token>

Response: {
  "id": 1,
  "accountNumber": "ACC001234567890",
  "accountType": "SAVINGS",
  "balance": 50000.00,
  "user": { "id": 1, "name": "John Doe" }
}
```

#### Get Account Balance
```http
GET /api/accounts/{accountId}/balance
Authorization: Bearer <token>

Response: { "balance": 50000.00 }
```

#### Submit Account Opening Request
```http
POST /api/accounts/request
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 1,
  "accountType": "SAVINGS",
  "ifscCode": "MVB001001",
  "branchName": "Main Branch",
  "branchAddress": "123 Main Street"
}

Response: {
  "id": 1,
  "userId": 1,
  "accountType": "SAVINGS",
  "status": "PENDING",
  "requestDate": "2025-07-06T10:30:00"
}
```

#### Get Account Requests by User
```http
GET /api/accounts/requests/user/{userId}
Authorization: Bearer <token>

Response: [
  {
    "id": 1,
    "userId": 1,
    "accountType": "SAVINGS",
    "status": "PENDING",
    "requestDate": "2025-07-06T10:30:00",
    "employeeComments": null
  }
]
```

#### Approve Account Request (Employee/Admin)
```http
POST /api/accounts/requests/{requestId}/approve
Authorization: Bearer <employee_token>
Content-Type: application/json

{
  "initialBalance": 1000.00,
  "comments": "Approved after verification"
}

Response: { "message": "Account request approved and account created" }
```

#### Reject Account Request (Employee/Admin)
```http
POST /api/accounts/requests/{requestId}/reject
Authorization: Bearer <employee_token>
Content-Type: application/json

{
  "comments": "Insufficient documentation"
}

Response: { "message": "Account request rejected" }
```

#### Open Account Directly (Admin/Employee)
```http
POST /api/accounts/open
Authorization: Bearer <employee_token>
Content-Type: application/json

{
  "userId": 1,
  "accountType": "SAVINGS",
  "ifscCode": "MVB001001",
  "branchName": "Main Branch",
  "branchAddress": "123 Main Street",
  "initialBalance": 1000.00
}

Response: {
  "id": 1,
  "accountNumber": "ACC001234567890",
  "accountType": "SAVINGS",
  "balance": 1000.00
}
```

### 💸 Transaction APIs

#### Transfer Funds
```http
POST /api/transactions/transfer
Authorization: Bearer <token>
Content-Type: application/json

{
  "fromAccountId": 1,
  "toAccountNumber": "ACC987654321",
  "amount": 1000.00,
  "description": "Fund transfer"
}

Response: {
  "id": 1,
  "type": "TRANSFER",
  "amount": 1000.00,
  "date": "2025-07-06T10:30:00",
  "description": "Fund transfer",
  "status": "COMPLETED"
}
```

#### Deposit Funds
```http
POST /api/transactions/deposit
Authorization: Bearer <token>
Content-Type: application/json

{
  "accountId": 1,
  "amount": 5000.00,
  "description": "Cash deposit"
}

Response: {
  "id": 2,
  "type": "DEPOSIT",
  "amount": 5000.00,
  "status": "COMPLETED"
}
```

#### Withdraw Funds
```http
POST /api/transactions/withdraw
Authorization: Bearer <token>
Content-Type: application/json

{
  "accountId": 1,
  "amount": 2000.00,
  "description": "ATM withdrawal"
}

Response: {
  "id": 3,
  "type": "WITHDRAW",
  "amount": 2000.00,
  "status": "COMPLETED"
}
```

#### Get Account Transactions
```http
GET /api/transactions/account/{accountId}
Authorization: Bearer <token>

Response: [
  {
    "id": 1,
    "type": "DEPOSIT",
    "amount": 5000.00,
    "date": "2025-07-06T10:30:00",
    "description": "Initial deposit",
    "status": "COMPLETED"
  }
]
```

#### Get User Transactions
```http
GET /api/transactions/user/{userId}
Authorization: Bearer <token>

Response: [
  {
    "id": 1,
    "type": "DEPOSIT",
    "amount": 5000.00,
    "date": "2025-07-06T10:30:00",
    "account": { "accountNumber": "ACC001234567890" }
  }
]
```

### 🏠 Loan Management APIs

#### Get Loan Types
```http
GET /api/loan-types
Authorization: Bearer <token>

Response: [
  {
    "id": 1,
    "typeName": "HOME",
    "description": "Home loan for property purchase",
    "interestRate": 8.5,
    "maxAmount": 10000000.00,
    "maxTenure": 300,
    "isActive": true
  }
]
```

#### Apply for Loan
```http
POST /api/loans/apply
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 1,
  "loanTypeId": 1,
  "amount": 500000.00,
  "purpose": "Home purchase",
  "tenure": 240
}

Response: {
  "id": 1,
  "amount": 500000.00,
  "status": "APPLIED",
  "applicationDate": "2025-07-06",
  "loanType": { "typeName": "HOME" }
}
```

#### Get User Loans
```http
GET /api/loans/user/{userId}
Authorization: Bearer <token>

Response: [
  {
    "id": 1,
    "amount": 500000.00,
    "interestRate": 8.5,
    "tenureMonths": 240,
    "status": "APPLIED",
    "loanType": { "typeName": "HOME" }
  }
]
```

#### Get All Loans (Employee/Admin)
```http
GET /api/loans
Authorization: Bearer <employee_token>

Response: [
  {
    "id": 1,
    "amount": 500000.00,
    "status": "APPLIED",
    "user": { "name": "John Doe" },
    "loanType": { "typeName": "HOME" }
  }
]
```

#### Update Loan Status (Employee/Admin)
```http
PUT /api/loans/{loanId}/status
Authorization: Bearer <employee_token>
Content-Type: application/json

{
  "status": "APPROVED",
  "employeeComments": "Loan approved after verification"
}

Response: {
  "id": 1,
  "status": "APPROVED",
  "employeeComments": "Loan approved after verification"
}
```

#### Check Loan Eligibility
```http
POST /api/loans/check-eligibility
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 1,
  "loanTypeId": 1,
  "amount": 500000.00
}

Response: {
  "eligible": true,
  "message": "You are eligible for this loan",
  "maxEligibleAmount": 800000.00
}
```

#### Calculate EMI
```http
POST /api/loans/calculate-emi
Authorization: Bearer <token>
Content-Type: application/json

{
  "principal": 500000.00,
  "interestRate": 8.5,
  "tenure": 240
}

Response: {
  "emi": 4321.50,
  "totalAmount": 1037160.00,
  "totalInterest": 537160.00
}
```

### 📱 Beneficiary Management APIs

#### Add Beneficiary
```http
POST /api/beneficiaries
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 1,
  "beneficiaryName": "Jane Doe",
  "accountNumber": "1234567890",
  "ifscCode": "SBI0001234",
  "bankName": "State Bank of India"
}

Response: {
  "id": 1,
  "beneficiaryName": "Jane Doe",
  "accountNumber": "1234567890",
  "bankName": "State Bank of India"
}
```

#### Get User Beneficiaries
```http
GET /api/beneficiaries/user/{userId}
Authorization: Bearer <token>

Response: [
  {
    "id": 1,
    "beneficiaryName": "Jane Doe",
    "accountNumber": "1234567890",
    "ifscCode": "SBI0001234",
    "bankName": "State Bank of India"
  }
]
```

#### Remove Beneficiary
```http
DELETE /api/beneficiaries/{beneficiaryId}
Authorization: Bearer <token>

Response: { "message": "Beneficiary removed successfully" }
```

### 🏦 Bank & Branch APIs

#### Get All Banks
```http
GET /api/banks
Authorization: Bearer <token>

Response: [
  {
    "id": 1,
    "bankName": "Maverick Bank",
    "bankCode": "MVB"
  }
]
```

#### Get Bank by ID
```http
GET /api/banks/{id}
Authorization: Bearer <token>

Response: {
  "id": 1,
  "bankName": "Maverick Bank",
  "bankCode": "MVB"
}
```

#### Get Branches by Bank Name
```http
GET /api/banks/{bankName}/branches
Authorization: Bearer <token>

Response: [
  {
    "id": 1,
    "branchName": "Main Branch",
    "branchCode": "001",
    "ifscCode": "MVB001001",
    "address": "123 Main Street",
    "contactNumber": "1234567890"
  }
]
```

#### Get Branch by IFSC
```http
GET /api/banks/branches/ifsc/{ifscCode}
Authorization: Bearer <token>

Response: {
  "id": 1,
  "branchName": "Main Branch",
  "ifscCode": "MVB001001",
  "address": "123 Main Street",
  "bank": { "bankName": "Maverick Bank" }
}
```

### 📊 Reports APIs (Admin/Employee)

#### Get All Accounts Report
```http
GET /api/reports/accounts
Authorization: Bearer <employee_token>

Response: [
  {
    "accountNumber": "ACC001234567890",
    "accountType": "SAVINGS",
    "balance": 50000.00,
    "user": { "name": "John Doe" }
  }
]
```

#### Get All Transactions Report
```http
GET /api/reports/transactions
Authorization: Bearer <employee_token>

Response: [
  {
    "type": "DEPOSIT",
    "amount": 5000.00,
    "date": "2025-07-06T10:30:00",
    "account": { "accountNumber": "ACC001234567890" }
  }
]
```

#### Get All Loans Report
```http
GET /api/reports/loans
Authorization: Bearer <employee_token>

Response: [
  {
    "amount": 500000.00,
    "status": "APPROVED",
    "user": { "name": "John Doe" },
    "loanType": { "typeName": "HOME" }
  }
]
```

---

## 📋 BUSINESS RULES & WORKFLOWS

### Account Opening Workflow
1. **Customer Request** → Submit account opening request
2. **Pending Review** → Employee reviews KYC documents
3. **Approval/Rejection** → Employee approves or rejects with comments
4. **Account Creation** → If approved, account is created with initial balance

### Loan Application Workflow
1. **Application** → Customer applies for loan with required details
2. **Status: APPLIED** → Loan enters pending queue
3. **Employee Review** → Employee reviews application and documents
4. **Approval/Rejection** → Employee approves, rejects, or requests more info
5. **Status: APPROVED/REJECTED** → Final status updated
6. **Disbursement** → If approved, funds are disbursed

### Transaction Rules
- **Minimum Balance:** ₹1000 for Savings accounts
- **Daily Transfer Limit:** ₹100,000
- **Transaction Types:** DEPOSIT, WITHDRAW, TRANSFER
- **Validation:** Sufficient balance check for withdrawals/transfers

### User Registration Rules
- **Username:** Must be unique
- **Password:** Minimum 8 characters
- **KYC Fields:** Aadhar and PAN required for account opening
- **Age Calculation:** Automatic from date of birth

---

## ⚠️ ERROR HANDLING

### Standard Error Response Format
```json
{
  "timestamp": "2025-07-06T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Insufficient balance for withdrawal",
  "path": "/api/transactions/withdraw"
}
```

### Common HTTP Status Codes
| Code | Meaning | Usage |
|------|---------|-------|
| 200 | OK | Successful requests |
| 201 | Created | Resource created successfully |
| 400 | Bad Request | Invalid request data |
| 401 | Unauthorized | Authentication required |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 500 | Internal Server Error | Server-side error |

### Validation Errors
- **Required Fields:** Field cannot be null or empty
- **Format Errors:** Invalid email, phone number, etc.
- **Business Rule Violations:** Insufficient balance, duplicate username, etc.

---

## 🧪 SAMPLE API REQUESTS/RESPONSES

### Complete Login Flow
```bash
# 1. Login
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Response:
{"token":"eyJhbGciOiJIUzUxMiJ9..."}

# 2. Use token for subsequent requests
curl -X GET http://localhost:9090/api/users/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

### Account Creation Flow
```bash
# 1. Register user
curl -X POST http://localhost:9090/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "password123",
    "name": "John Doe",
    "email": "john@example.com",
    "contactNumber": "9876543210",
    "address": "123 Main Street",
    "dateOfBirth": "1990-01-15",
    "aadharNumber": "123456789012",
    "panNumber": "ABCDE1234F",
    "gender": "MALE"
  }'

# 2. Login as user
# 3. Submit account request
# 4. Employee approves request
# 5. Account is created
```

### Transaction Flow
```bash
# 1. Check balance
curl -X GET http://localhost:9090/api/accounts/1/balance \
  -H "Authorization: Bearer <token>"

# 2. Transfer funds
curl -X POST http://localhost:9090/api/transactions/transfer \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountId": 1,
    "toAccountNumber": "ACC987654321",
    "amount": 1000.00,
    "description": "Payment"
  }'
```

---

## 🎨 FRONTEND INTEGRATION GUIDELINES

### Angular Service Structure
```typescript
// auth.service.ts
@Injectable()
export class AuthService {
  login(credentials): Observable<AuthResponse>
  register(userData): Observable<any>
  logout(): void
  getToken(): string
  isAuthenticated(): boolean
}

// api.service.ts
@Injectable()
export class ApiService {
  private baseUrl = 'http://localhost:9090/api'
  
  // Add Authorization header to all requests
  get(url): Observable<any>
  post(url, data): Observable<any>
  put(url, data): Observable<any>
  delete(url): Observable<any>
}
```

### HTTP Interceptor for JWT
```typescript
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    return next.handle(req);
  }
}
```

### Error Handling
```typescript
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Redirect to login
        }
        return throwError(error);
      })
    );
  }
}
```

### Route Guards
```typescript
@Injectable()
export class AuthGuard implements CanActivate {
  canActivate(): boolean {
    return this.authService.isAuthenticated();
  }
}

@Injectable()
export class RoleGuard implements CanActivate {
  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRole = route.data.expectedRole;
    const userRole = this.authService.getUserRole();
    return userRole === expectedRole;
  }
}
```

---

## ✅ TESTING & VALIDATION

### API Testing Checklist
- [ ] **Authentication:** Login/logout functionality
- [ ] **User Registration:** Customer and employee registration
- [ ] **Account Management:** CRUD operations
- [ ] **Transactions:** Deposit, withdraw, transfer
- [ ] **Loans:** Application and approval workflow
- [ ] **Beneficiaries:** Add/remove beneficiaries
- [ ] **Error Handling:** Proper error responses
- [ ] **Authorization:** Role-based access control

### Test Data Available
- **Admin User:** admin/admin123
- **Employee User:** employee/employee123
- **Sample Banks:** Maverick Bank, State Bank of India
- **Sample Branches:** Main Branch (MVB001001)
- **Loan Types:** HOME, CAR, PERSONAL, EDUCATION, BUSINESS

### Swagger UI Testing
Access http://localhost:9090/swagger-ui.html for interactive API testing:
1. Use the "Authorize" button to set JWT token
2. Test all endpoints with sample data
3. Verify request/response formats

---

## 🎯 FRONTEND DEVELOPMENT RECOMMENDATIONS

### UI/UX Guidelines
1. **Dashboard:** Show account balances, recent transactions, quick actions
2. **Navigation:** Sidebar with modules (Accounts, Transactions, Loans, etc.)
3. **Forms:** Use validation for all inputs
4. **Notifications:** Show success/error messages for all actions
5. **Loading States:** Show spinners during API calls
6. **Responsive Design:** Mobile-friendly interface

### Key Features to Implement
1. **Authentication Pages:** Login, register, forgot password
2. **Customer Dashboard:** Account overview, quick actions
3. **Account Management:** View accounts, request new account
4. **Transaction Module:** Transfer funds, view history
5. **Loan Module:** Apply for loans, track status
6. **Beneficiary Management:** Add/manage beneficiaries
7. **Profile Management:** Update user information
8. **Admin Panel:** User management, reports
9. **Employee Panel:** Approve requests, manage loans

### State Management
Consider using NgRx or Akita for:
- User authentication state
- Account balances
- Transaction history
- Loading states

---

## 🔥 LOGO PROMPT FOR AI GENERATION

**Prompt for AI Logo Generation:**

> Create a modern, professional banking logo for "Maverick Bank" with the following specifications:
> 
> **Style:** Clean, minimalist, trustworthy fintech design
> **Colors:** Deep navy blue (#1E3A8A) as primary, bright cyan (#06B6D4) as accent
> **Elements:** Incorporate subtle geometric shapes suggesting growth, security, and innovation
> **Typography:** Modern sans-serif font for "MAVERICK BANK" 
> **Symbol:** Abstract icon combining a shield (security) with upward arrow (growth) or digital wave patterns
> **Feel:** Premium, digital-first, startup energy but with banking trust and stability
> **Format:** SVG vector, scalable, works on both dark and light backgrounds
> **Usage:** Mobile app icon, website header, business cards
> 
> The logo should convey innovation, trust, security, and digital banking excellence - suitable for a modern fintech startup that rivals top-tier banking platforms.

---

## 🚀 READY TO BUILD!

With this comprehensive reference, you have everything needed to build a complete, professional Angular frontend for Maverick Bank. The backend is fully functional, documented, and ready for integration.

**Start with authentication, then build feature by feature. Good luck! 🏦✨**

---

*Last Updated: July 6, 2025*  
*Backend Version: 1.0*  
*API Base URL: http://localhost:9090*
