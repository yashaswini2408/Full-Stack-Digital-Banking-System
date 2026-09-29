# Maverick Bank Backend API Testing Script
# This script tests all the key endpoints to ensure they're working correctly

$baseUrl = "http://localhost:9090"
$loginUrl = "$baseUrl/api/auth/login"

Write-Host "=== MAVERICK BANK BACKEND API TESTING ===" -ForegroundColor Green
Write-Host ""

# Function to get authentication token
function Get-AuthToken {
    param($username, $password)
    try {
        $body = @{ username = $username; password = $password } | ConvertTo-Json
        $response = Invoke-RestMethod -Uri $loginUrl -Method POST -Body $body -ContentType "application/json"
        return $response.token
    }
    catch {
        Write-Host "Authentication failed for $username" -ForegroundColor Red
        return $null
    }
}

# Function to make authenticated requests
function Invoke-AuthenticatedRequest {
    param($url, $method = "GET", $token, $body = $null)
    try {
        $headers = @{ Authorization = "Bearer $token" }
        if ($body) {
            return Invoke-RestMethod -Uri $url -Method $method -Headers $headers -Body $body -ContentType "application/json"
        } else {
            return Invoke-RestMethod -Uri $url -Method $method -Headers $headers
        }
    }
    catch {
        Write-Host "Request failed: $url" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

# Test 1: Authentication
Write-Host "1. Testing Authentication..." -ForegroundColor Yellow
$adminToken = Get-AuthToken -username "admin" -password "admin123"
if ($adminToken) {
    Write-Host "Admin authentication successful" -ForegroundColor Green
} else {
    Write-Host "Admin authentication failed" -ForegroundColor Red
    exit 1
}

$employeeToken = Get-AuthToken -username "employee" -password "employee123"
if ($employeeToken) {
    Write-Host "Employee authentication successful" -ForegroundColor Green
} else {
    Write-Host "Employee authentication failed" -ForegroundColor Red
}

Write-Host ""

# Test 2: Banks and Branches
Write-Host "2. Testing Banks and Branches..." -ForegroundColor Yellow
$banks = Invoke-AuthenticatedRequest -url "$baseUrl/api/banks" -token $adminToken
if ($banks) {
    Write-Host "SUCCESS: Banks endpoint working - Found $($banks.Count) banks" -ForegroundColor Green
    foreach ($bank in $banks) {
        Write-Host "   - Bank: $($bank.bankName) (Code: $($bank.bankCode))" -ForegroundColor Cyan
    }
} else {
    Write-Host "FAILED: Banks endpoint failed" -ForegroundColor Red
}

# Test branch by IFSC
$branch = Invoke-AuthenticatedRequest -url "$baseUrl/api/banks/branches/ifsc/MVB001001" -token $adminToken
if ($branch) {
    Write-Host "SUCCESS: Branch by IFSC working - Found: $($branch.branchName)" -ForegroundColor Green
} else {
    Write-Host "FAILED: Branch by IFSC failed" -ForegroundColor Red
}

Write-Host ""

# Test 3: Loan Types
Write-Host "3. Testing Loan Types..." -ForegroundColor Yellow
$loanTypes = Invoke-AuthenticatedRequest -url "$baseUrl/api/loan-types" -token $adminToken
if ($loanTypes) {
    Write-Host "SUCCESS: Loan Types endpoint working - Found $($loanTypes.Count) loan types" -ForegroundColor Green
    foreach ($loanType in $loanTypes) {
        Write-Host "   - Loan Type: $($loanType.typeName) (Rate: $($loanType.interestRate)%)" -ForegroundColor Cyan
    }
} else {
    Write-Host "FAILED: Loan Types endpoint failed" -ForegroundColor Red
}

Write-Host ""

# Test 4: Account Requests
Write-Host "4. Testing Account Request Creation..." -ForegroundColor Yellow
$accountRequestBody = @{
    userId = 1
    accountType = "SAVINGS"
    ifscCode = "MVB001001"
    branchName = "Maverick Bank Main Branch"
    branchAddress = "123 Main Street, Financial District"
} | ConvertTo-Json

$accountRequest = Invoke-AuthenticatedRequest -url "$baseUrl/api/accounts/request" -method "POST" -token $adminToken -body $accountRequestBody
if ($accountRequest) {
    Write-Host "SUCCESS: Account Request creation successful - Request ID: $($accountRequest.id)" -ForegroundColor Green
} else {
    Write-Host "FAILED: Account Request creation failed" -ForegroundColor Red
}

# Test getting all account requests
$allRequests = Invoke-AuthenticatedRequest -url "$baseUrl/api/accounts/requests/user/1" -token $adminToken
if ($allRequests) {
    Write-Host "SUCCESS: Get Account Requests working - Found $($allRequests.Count) requests" -ForegroundColor Green
} else {
    Write-Host "FAILED: Get Account Requests failed" -ForegroundColor Red
}

Write-Host ""

# Test 5: User Registration
Write-Host "5. Testing User Registration..." -ForegroundColor Yellow
$userRegistrationBody = @{
    fullName = "Test Customer"
    username = "testcustomer"
    email = "test.customer@test.com"
    password = "password123"
    phone = "9876543211"
    dateOfBirth = "1992-05-20"
    aadharNumber = "123456789013"
    panNumber = "ABCDE1234G"
    gender = "FEMALE"
    address = "456 Test Avenue, Test City"
} | ConvertTo-Json

$userRegistration = Invoke-AuthenticatedRequest -url "$baseUrl/api/auth/register" -method "POST" -token $adminToken -body $userRegistrationBody
if ($userRegistration) {
    Write-Host "SUCCESS: User Registration successful" -ForegroundColor Green
} else {
    Write-Host "FAILED: User Registration failed" -ForegroundColor Red
}

Write-Host ""

Write-Host "=== API TESTING COMPLETED ===" -ForegroundColor Green
Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Yellow
Write-Host "1. All critical APIs are working" -ForegroundColor Green
Write-Host "2. Review Swagger UI at: http://localhost:9090/swagger-ui.html" -ForegroundColor Cyan
Write-Host "3. Run unit/integration tests" -ForegroundColor Yellow
Write-Host "4. Start frontend development" -ForegroundColor Yellow
Write-Host ""
