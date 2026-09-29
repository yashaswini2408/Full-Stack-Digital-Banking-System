# Full-Stack Digital Banking System

A full-stack digital banking platform built using **Spring Boot, MySQL, and React**. The system simulates real-world banking operations such as account management, fund transactions, loan processing, and beneficiary management.

---

## Technologies Used

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven
- MySQL
- Swagger / OpenAPI

### Frontend
- React
- TypeScript / JavaScript
- Bootstrap / Material UI
- REST API Integration

### Tools
- Git & GitHub
- Postman
- Swagger UI
- Docker (Optional)

---

## Key Features

### Customer
- Register and login
- Open bank accounts
- Deposit funds
- Withdraw funds
- Transfer funds
- View transaction history
- Add beneficiaries
- Apply for loans

### Employee
- Review transactions
- Approve or reject loan applications
- Monitor customer accounts

### Admin
- Manage users and employees
- Generate system reports

---

## System Architecture

```text
React Frontend
      ↓
Spring Boot REST API
      ↓
Service Layer
      ↓
Repository Layer (JPA)
      ↓
MySQL Database
