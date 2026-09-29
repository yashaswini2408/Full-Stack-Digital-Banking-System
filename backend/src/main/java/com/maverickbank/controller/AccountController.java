package com.maverickbank.controller;

import com.maverickbank.entity.Account;
import com.maverickbank.entity.AccountRequest;
import com.maverickbank.service.AccountService;
import com.maverickbank.service.AccountRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRequestService accountRequestService;

    @Operation(summary = "Get accounts by user ID", description = "Returns all accounts for a specific user.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUser(
            @Parameter(name = "userId", description = "User ID", required = true, example = "1")
            @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @Operation(summary = "Get account by account number", description = "Returns an account by its account number.")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(
            @Parameter(name = "accountNumber", description = "Account Number", required = true, example = "1234567890")
            @PathVariable(name = "accountNumber") String accountNumber) {
        return accountService.getAccountByNumber(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Submit account opening request", description = "Submit a request to open a new account")
    @PostMapping("/request")
    public ResponseEntity<AccountRequest> submitAccountRequest(@RequestBody AccountOpenRequestDto request) {
        try {
            AccountRequest accountRequest = accountRequestService.submitAccountRequest(
                request.getUserId(),
                request.getAccountType(),
                request.getIfscCode(),
                request.getBranchName(),
                request.getBranchAddress()
            );
            return ResponseEntity.ok(accountRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get account requests by user", description = "Get all account requests for a user")
    @GetMapping("/requests/user/{userId}")
    public ResponseEntity<List<AccountRequest>> getAccountRequestsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(accountRequestService.getRequestsByUserId(userId));
    }

    @Operation(summary = "Open account directly", description = "Open account directly (Admin/Employee only)")
    @PostMapping("/open")
    public ResponseEntity<Account> openAccount(@RequestBody AccountOpenRequest request) {
        try {
            Account account = accountService.openAccount(
                request.getUserId(),
                request.getAccountType(),
                request.getIfscCode(),
                request.getBranchName(),
                request.getBranchAddress(),
                request.getInitialBalance()
            );
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Employee endpoints
    @Operation(summary = "Get pending account requests", description = "Get all pending account requests (Employee only)")
    @GetMapping("/requests/pending")
    public ResponseEntity<List<AccountRequest>> getPendingRequests() {
        return ResponseEntity.ok(accountRequestService.getPendingRequests());
    }

    @Operation(summary = "Approve account request", description = "Approve an account request (Employee only)")
    @PostMapping("/requests/{requestId}/approve")
    public ResponseEntity<AccountRequest> approveAccountRequest(
            @PathVariable Long requestId, 
            @RequestBody ApprovalRequest request) {
        try {
            AccountRequest approved = accountRequestService.approveRequest(
                requestId, 
                request.getEmployeeId(), 
                request.getComments()
            );
            return ResponseEntity.ok(approved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Reject account request", description = "Reject an account request (Employee only)")
    @PostMapping("/requests/{requestId}/reject")
    public ResponseEntity<AccountRequest> rejectAccountRequest(
            @PathVariable Long requestId, 
            @RequestBody ApprovalRequest request) {
        try {
            AccountRequest rejected = accountRequestService.rejectRequest(
                requestId, 
                request.getEmployeeId(), 
                request.getComments()
            );
            return ResponseEntity.ok(rejected);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DTOs
    public static class AccountOpenRequestDto {
        private Long userId;
        private String accountType;
        private String ifscCode;
        private String branchName;
        private String branchAddress;

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getAccountType() { return accountType; }
        public void setAccountType(String accountType) { this.accountType = accountType; }
        public String getIfscCode() { return ifscCode; }
        public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
        public String getBranchName() { return branchName; }
        public void setBranchName(String branchName) { this.branchName = branchName; }
        public String getBranchAddress() { return branchAddress; }
        public void setBranchAddress(String branchAddress) { this.branchAddress = branchAddress; }
    }

    public static class AccountOpenRequest {
        private Long userId;
        private String accountType;
        private String ifscCode;
        private String branchName;
        private String branchAddress;
        private BigDecimal initialBalance;

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getAccountType() { return accountType; }
        public void setAccountType(String accountType) { this.accountType = accountType; }
        public String getIfscCode() { return ifscCode; }
        public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
        public String getBranchName() { return branchName; }
        public void setBranchName(String branchName) { this.branchName = branchName; }
        public String getBranchAddress() { return branchAddress; }
        public void setBranchAddress(String branchAddress) { this.branchAddress = branchAddress; }
        public BigDecimal getInitialBalance() { return initialBalance; }
        public void setInitialBalance(BigDecimal initialBalance) { this.initialBalance = initialBalance; }
    }

    public static class ApprovalRequest {
        private Long employeeId;
        private String comments;

        public Long getEmployeeId() { return employeeId; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
    }
}
