# Maverick Bank Backend - Quick API Reference

## 🚀 Getting Started

### Base URL
```
http://localhost:9090
```

### Authentication
All APIs (except login/register) require JWT token in Authorization header:
```
Authorization: Bearer <token>
```

## 🔐 Authentication APIs

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Response: { "token": "jwt_token_here" }
```

### Register
```http
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "John Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "9876543210",
  "dateOfBirth": "1990-01-15",
  "aadharNumber": "123456789012",
  "panNumber": "ABCDE1234F",
  "gender": "MALE",
  "address": "123 Main Street"
}
```

## 🏦 Banking APIs

### Get Banks
```http
GET /api/banks
```

### Get Branches by Bank
```http
GET /api/banks/{bankName}/branches
```

### Get Branch by IFSC
```http
GET /api/banks/branches/ifsc/{ifscCode}
```

## 👤 User Management

### Get Profile
```http
GET /api/users/profile
```

### Update Profile
```http
PUT /api/users/profile
Content-Type: application/json

{
  "fullName": "Updated Name",
  "email": "updated@example.com",
  "phone": "9876543210",
  "address": "Updated Address"
}
```

## 💳 Account Management

### Request Account Opening
```http
POST /api/accounts/request
Content-Type: application/json

{
  "userId": 1,
  "accountType": "SAVINGS",
  "ifscCode": "MVB001001",
  "branchName": "Main Branch",
  "branchAddress": "123 Main Street"
}
```

### Get User Account Requests
```http
GET /api/accounts/requests/user/{userId}
```

### Get User Accounts
```http
GET /api/accounts/user/{userId}
```

### Get Account Balance
```http
GET /api/accounts/{accountId}/balance
```

## 💸 Transaction APIs

### Transfer Funds
```http
POST /api/transactions/transfer
Content-Type: application/json

{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 1000.00,
  "description": "Transfer description"
}
```

### Get Account Transactions
```http
GET /api/transactions/account/{accountId}
```

### Get User Transactions
```http
GET /api/transactions/user/{userId}
```

## 🏠 Loan Management

### Apply for Loan
```http
POST /api/loans/apply
Content-Type: application/json

{
  "userId": 1,
  "loanTypeId": 1,
  "amount": 500000.00,
  "purpose": "Home purchase",
  "tenure": 240
}
```

### Get User Loans
```http
GET /api/loans/user/{userId}
```

### Get Loan Types
```http
GET /api/loan-types
```

## 📱 Beneficiary Management

### Add Beneficiary
```http
POST /api/beneficiaries
Content-Type: application/json

{
  "userId": 1,
  "beneficiaryName": "Jane Doe",
  "accountNumber": "1234567890",
  "ifscCode": "SBI0001234",
  "bankName": "State Bank of India"
}
```

### Get User Beneficiaries
```http
GET /api/beneficiaries/user/{userId}
```

## 🔧 Admin/Employee APIs

### Approve Account Request
```http
POST /api/accounts/requests/{requestId}/approve
```

### Reject Account Request
```http
POST /api/accounts/requests/{requestId}/reject
```

### Update Loan Status
```http
PUT /api/loans/{loanId}/status
Content-Type: application/json

{
  "status": "APPROVED",
  "employeeComments": "Loan approved after verification"
}
```

## 📊 Response Formats

### Success Response
```json
{
  "status": "success",
  "data": { ... }
}
```

### Error Response
```json
{
  "status": "error",
  "message": "Error description",
  "timestamp": "2025-07-06T10:30:00"
}
```

## 🧪 Test Credentials

### Admin User
- Username: `admin`
- Password: `admin123`

### Employee User  
- Username: `employee`
- Password: `employee123`

## 📖 Full Documentation
Swagger UI: http://localhost:9090/swagger-ui.html

---

*This is a quick reference. See Swagger UI for complete API documentation with examples.*
