# Maverick Bank Backend API Testing Script
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
        return $null
    }
}

# Test Authentication
Write-Host "1. Testing Authentication..." -ForegroundColor Yellow
$adminToken = Get-AuthToken -username "admin" -password "admin123"
if ($adminToken) {
    Write-Host "SUCCESS: Admin authentication" -ForegroundColor Green
} else {
    Write-Host "FAILED: Admin authentication" -ForegroundColor Red
    exit 1
}

# Test Banks
Write-Host "2. Testing Banks..." -ForegroundColor Yellow
$banks = Invoke-AuthenticatedRequest -url "$baseUrl/api/banks" -token $adminToken
if ($banks) {
    Write-Host "SUCCESS: Banks endpoint - Found $($banks.Count) banks" -ForegroundColor Green
} else {
    Write-Host "FAILED: Banks endpoint" -ForegroundColor Red
}

# Test Loan Types
Write-Host "3. Testing Loan Types..." -ForegroundColor Yellow
$loanTypes = Invoke-AuthenticatedRequest -url "$baseUrl/api/loan-types" -token $adminToken
if ($loanTypes) {
    Write-Host "SUCCESS: Loan Types endpoint - Found $($loanTypes.Count) loan types" -ForegroundColor Green
} else {
    Write-Host "FAILED: Loan Types endpoint" -ForegroundColor Red
}

# Test Account Request Creation
Write-Host "4. Testing Account Request..." -ForegroundColor Yellow
$accountRequestBody = @{
    fullName = "John Doe"
    email = "john.doe@test.com"
    phone = "9876543210"
    dateOfBirth = "1990-01-15"
    aadharNumber = "123456789012"
    panNumber = "ABCDE1234F"
    gender = "MALE"
    address = "123 Test Street"
    initialDeposit = 10000
    accountType = "SAVINGS"
    branchId = 1
} | ConvertTo-Json

$accountRequest = Invoke-AuthenticatedRequest -url "$baseUrl/api/accounts/request" -method "POST" -token $adminToken -body $accountRequestBody
if ($accountRequest) {
    Write-Host "SUCCESS: Account Request creation" -ForegroundColor Green
} else {
    Write-Host "FAILED: Account Request creation" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== TESTING COMPLETED ===" -ForegroundColor Green
Write-Host "Swagger UI: http://localhost:9090/swagger-ui.html" -ForegroundColor Cyan
