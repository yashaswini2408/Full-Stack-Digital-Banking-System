package com.maverickbank.controller;

import com.maverickbank.entity.Transaction;
import com.maverickbank.service.TransactionService;
import com.maverickbank.dto.TransferToBeneficiaryRequest;
import com.maverickbank.dto.TransferByAccountNumberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Get transactions by account ID", description = "Returns all transactions for a specific account.")
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(
            @Parameter(name = "accountId", description = "Account ID", required = true, example = "1")
            @PathVariable(name = "accountId") Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }

    @Operation(summary = "Get recent transactions by account", description = "Get recent transactions for an account with limit")
    @GetMapping("/account/{accountId}/recent")
    public ResponseEntity<List<Transaction>> getRecentTransactionsByAccount(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(transactionService.getRecentTransactionsByAccountId(accountId, limit));
    }

    @Operation(summary = "Get transactions by date range", description = "Get transactions for an account within date range")
    @GetMapping("/account/{accountId}/date-range")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
            @PathVariable Long accountId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
            LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);
            return ResponseEntity.ok(transactionService.getTransactionsByAccountIdAndDateRange(accountId, start, end));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get recent transactions by user", description = "Get recent transactions for all user accounts")
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<Transaction>> getRecentTransactionsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(transactionService.getRecentTransactionsByUserId(userId, limit));
    }

    @Operation(summary = "Get monthly transactions", description = "Get transactions for a user in a specific month")
    @GetMapping("/user/{userId}/monthly")
    public ResponseEntity<List<Transaction>> getMonthlyTransactions(
            @PathVariable Long userId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(transactionService.getMonthlyTransactionsByUserId(userId, month, year));
    }

    @Operation(summary = "Deposit funds", description = "Deposit money to an account")
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody TransactionRequest request) {
        try {
            return ResponseEntity.ok(transactionService.deposit(request.getAccountId(), request.getAmount()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Withdraw funds", description = "Withdraw money from an account")
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody TransactionRequest request) {
        try {
            return ResponseEntity.ok(transactionService.withdraw(request.getAccountId(), request.getAmount()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Transfer funds", description = "Transfer money between accounts")
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransferRequest request) {
        try {
            return ResponseEntity.ok(transactionService.transfer(
                request.getFromAccountId(), 
                request.getToAccountId(), 
                request.getAmount()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Transfer funds to beneficiary", description = "Transfer money to a beneficiary account")
    @PostMapping("/transfer-to-beneficiary")
    public ResponseEntity<Transaction> transferToBeneficiary(@RequestBody TransferToBeneficiaryRequest request) {
        try {
            return ResponseEntity.ok(transactionService.transferToBeneficiary(
                request.getFromAccountId(), 
                request.getBeneficiaryId(), 
                request.getAmount(),
                request.getDescription()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all transactions", description = "Get all transactions (Employee/Admin only)")
    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @Operation(summary = "Transfer funds by account number", description = "Transfer money between accounts using account numbers")
    @PostMapping("/transfer-by-account-number")
    public ResponseEntity<Transaction> transferByAccountNumber(@RequestBody TransferByAccountNumberRequest request) {
        try {
            return ResponseEntity.ok(transactionService.transferByAccountNumber(
                request.getFromAccountId(), 
                request.getToAccountNumber(), 
                request.getAmount(),
                request.getDescription()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class TransactionRequest {
        private Long accountId;
        private BigDecimal amount;
        private String description;

        public Long getAccountId() { return accountId; }
        public void setAccountId(Long accountId) { this.accountId = accountId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class TransferRequest {
        private Long fromAccountId;
        private Long toAccountId;
        private BigDecimal amount;
        private String description;

        public Long getFromAccountId() { return fromAccountId; }
        public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }
        public Long getToAccountId() { return toAccountId; }
        public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class TransferToBeneficiaryRequest {
        private Long fromAccountId;
        private Long beneficiaryId;
        private BigDecimal amount;
        private String description;

        public Long getFromAccountId() { return fromAccountId; }
        public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }
        public Long getBeneficiaryId() { return beneficiaryId; }
        public void setBeneficiaryId(Long beneficiaryId) { this.beneficiaryId = beneficiaryId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class TransferByAccountNumberRequest {
        private Long fromAccountId;
        private String toAccountNumber;
        private BigDecimal amount;
        private String description;

        public Long getFromAccountId() { return fromAccountId; }
        public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }
        public String getToAccountNumber() { return toAccountNumber; }
        public void setToAccountNumber(String toAccountNumber) { this.toAccountNumber = toAccountNumber; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
