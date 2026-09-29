# MAVERICK BANK - ACCOUNT CONTROLLER API TEST DATA FOR SWAGGER

## AUTHENTICATION FIRST
Before testing any account endpoints, you need to authenticate:

### 1. LOGIN (POST /api/auth/login)
```json
{
  "username": "admin",
  "password": "admin123"
}
```
**Response will contain a JWT token - copy this token for Authorization header**

---

## ACCOUNT CONTROLLER ENDPOINTS TEST DATA

### 2. GET ACCOUNTS BY USER ID (GET /api/accounts/user/{userId})
**URL:** `/api/accounts/user/1`
**Headers:** 
- Authorization: Bearer {your-jwt-token}
- Accept: application/json

**Expected Response:** List of accounts for user ID 1

---

### 3. GET ACCOUNT BY ACCOUNT NUMBER (GET /api/accounts/{accountNumber})
**URL:** `/api/accounts/ACCT1234567890`
**Headers:** 
- Authorization: Bearer {your-jwt-token}
- Accept: application/json

**Expected Response:** Single account details

---

### 4. SUBMIT ACCOUNT REQUEST (POST /api/accounts/request)
**Headers:** 
- Authorization: Bearer {your-jwt-token}
- Content-Type: application/json

**Request Body:**
```json
{
  "userId": 2,
  "accountType": "SAVINGS",
  "ifscCode": "MVB0001001",
  "branchName": "Main Branch",
  "branchAddress": "123 Main Street"
}
```

**Alternative Test Data:**
```json
{
  "userId": 1,
  "accountType": "CURRENT",
  "ifscCode": "MVB0001002",
  "branchName": "Central Branch",
  "branchAddress": "456 Central Plaza"
}
```

---

### 5. GET ACCOUNT REQUESTS BY USER (GET /api/accounts/requests/user/{userId})
**URL:** `/api/accounts/requests/user/1`
**Headers:** 
- Authorization: Bearer {your-jwt-token}
- Accept: application/json

**Expected Response:** List of account requests for user ID 1

---

### 6. OPEN ACCOUNT DIRECTLY (POST /api/accounts/open)
**Headers:** 
- Authorization: Bearer {your-jwt-token}
- Content-Type: application/json

**Request Body:**
```json
{
  "userId": 2,
  "accountType": "SAVINGS",
  "ifscCode": "MVB0001001",
  "branchName": "Main Branch",
  "branchAddress": "123 Main Street",
  "initialBalance": 10000.00
}
```

**Alternative Test Data:**
```json
{
  "userId": 3,
  "accountType": "CURRENT",
  "ifscCode": "SBI0001233",
  "branchName": "SBI Branch",
  "branchAddress": "456 Business District",
  "initialBalance": 25000.00
}
```

---

### 7. GET PENDING ACCOUNT REQUESTS (GET /api/accounts/requests/pending)
**URL:** `/api/accounts/requests/pending`
**Headers:** 
- Authorization: Bearer {your-jwt-token}
- Accept: application/json

**Expected Response:** List of all pending account requests

---

### 8. APPROVE ACCOUNT REQUEST (POST /api/accounts/requests/{requestId}/approve)
**URL:** `/api/accounts/requests/1/approve` (replace 1 with actual request ID)
**Headers:** 
- Authorization: Bearer {your-jwt-token}
- Content-Type: application/json

**Request Body:**
```json
{
  "employeeId": 3,
  "comments": "Account request approved after verification of documents"
}
```

**Alternative Test Data:**
```json
{
  "employeeId": 4,
  "comments": "Approved - All KYC documents verified successfully"
}
```

---

### 9. REJECT ACCOUNT REQUEST (POST /api/accounts/requests/{requestId}/reject)
**URL:** `/api/accounts/requests/2/reject` (replace 2 with actual request ID)
**Headers:** 
- Authorization: Bearer {your-jwt-token}
- Content-Type: application/json

**Request Body:**
```json
{
  "employeeId": 3,
  "comments": "Request rejected - Incomplete documentation provided"
}
```

**Alternative Test Data:**
```json
{
  "employeeId": 4,
  "comments": "Rejected - Invalid IFSC code provided"
}
```

---

## VALID IFSC CODES FOR TESTING:
- MVB0001001 (Maverick Bank Main Branch)
- MVB0001002 (Maverick Bank Central Branch)  
- SBI0001233 (State Bank Branch)
- UNI0001234 (Union Bank Branch)

## VALID USER IDs FOR TESTING:
- 1 (Yash - Customer)
- 2 (John Doe - Customer)
- 3 (Admin - Administrator)
- 4 (Employee - Bank Employee)

## VALID ACCOUNT TYPES:
- SAVINGS
- CURRENT

## EXISTING ACCOUNT NUMBERS FOR TESTING:
- ACCT1234567890
- ACCT1750910699296
- ACCT1751009023971
- ACCT1751050512241

---

## TESTING WORKFLOW IN SWAGGER:

1. **First:** Login using `/api/auth/login` to get JWT token
2. **Copy** the token from response
3. **Click** 🔒 Authorize button in Swagger UI
4. **Enter:** `Bearer {your-jwt-token}` in the Authorization field
5. **Test** all endpoints in any order

## NOTES:
- All account endpoints require authentication
- Use realistic data for better testing
- Check response status codes (200 for success, 400 for bad request, etc.)
- Account numbers are auto-generated for new accounts
- Request IDs are auto-incremented starting from 1
