package com.maverickbank.controller;

import com.maverickbank.entity.Loan;
import com.maverickbank.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Operation(summary = "Get all loans", description = "Returns a list of all loans (Employee/Admin only)")
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @Operation(summary = "Get pending loans", description = "Get all pending loan applications (Employee only)")
    @GetMapping("/pending")
    public ResponseEntity<List<Loan>> getPendingLoans() {
        return ResponseEntity.ok(loanService.getPendingLoans());
    }

    @Operation(summary = "Get loan by ID", description = "Returns a loan by its ID")
    @GetMapping("/{loanId}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long loanId) {
        try {
            return ResponseEntity.ok(loanService.getLoanById(loanId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get loans by user ID", description = "Returns all loans for a specific user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> getLoansByUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(loanService.getLoansByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Apply for a loan", description = "Apply for a new loan")
    @PostMapping("/apply")
    public ResponseEntity<Loan> applyLoan(@RequestBody LoanApplicationRequest request) {
        try {
            Loan loan = loanService.applyLoan(
                request.getUserId(),
                request.getLoanTypeId(),
                request.getAmount(),
                request.getTenureMonths(),
                request.getPurpose()
            );
            return ResponseEntity.ok(loan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Approve loan", description = "Approve a loan application (Employee only)")
    @PostMapping("/{loanId}/approve")
    public ResponseEntity<Loan> approveLoan(
            @PathVariable Long loanId,
            @RequestBody LoanActionRequest request) {
        try {
            Loan loan = loanService.approveLoan(loanId, request.getEmployeeId(), request.getComments());
            return ResponseEntity.ok(loan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Reject loan", description = "Reject a loan application (Employee only)")
    @PostMapping("/{loanId}/reject")
    public ResponseEntity<Loan> rejectLoan(
            @PathVariable Long loanId,
            @RequestBody LoanActionRequest request) {
        try {
            Loan loan = loanService.rejectLoan(loanId, request.getEmployeeId(), request.getComments());
            return ResponseEntity.ok(loan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Disburse loan", description = "Disburse an approved loan (Employee only)")
    @PostMapping("/{loanId}/disburse")
    public ResponseEntity<Loan> disburseLoan(
            @PathVariable Long loanId,
            @RequestBody LoanActionRequest request) {
        try {
            Loan loan = loanService.disburseLoan(loanId, request.getEmployeeId());
            return ResponseEntity.ok(loan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Calculate EMI", description = "Calculate EMI for loan amount")
    @PostMapping("/calculate-emi")
    public ResponseEntity<Map<String, Object>> calculateEMI(@RequestBody EMICalculationRequest request) {
        try {
            BigDecimal emi = loanService.calculateEMI(
                request.getPrincipal(),
                request.getAnnualRate(),
                request.getTenureMonths()
            );
            return ResponseEntity.ok(Map.of(
                "emi", emi,
                "totalAmount", emi.multiply(BigDecimal.valueOf(request.getTenureMonths())),
                "totalInterest", emi.multiply(BigDecimal.valueOf(request.getTenureMonths())).subtract(request.getPrincipal())
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Check loan eligibility", description = "Check if user is eligible for loan amount")
    @PostMapping("/check-eligibility")
    public ResponseEntity<Map<String, Object>> checkEligibility(@RequestBody EligibilityRequest request) {
        try {
            boolean eligible = loanService.checkLoanEligibility(request.getUserId(), request.getAmount());
            return ResponseEntity.ok(Map.of("eligible", eligible));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DTOs
    public static class LoanApplicationRequest {
        private Long userId;
        private Long loanTypeId;
        private BigDecimal amount;
        private int tenureMonths;
        private String purpose;

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getLoanTypeId() { return loanTypeId; }
        public void setLoanTypeId(Long loanTypeId) { this.loanTypeId = loanTypeId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public int getTenureMonths() { return tenureMonths; }
        public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }
        public String getPurpose() { return purpose; }
        public void setPurpose(String purpose) { this.purpose = purpose; }
    }

    public static class LoanActionRequest {
        private Long employeeId;
        private String comments;

        public Long getEmployeeId() { return employeeId; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
        public String getComments() { return comments; }
        public void setComments(String comments) { this.comments = comments; }
    }

    public static class EMICalculationRequest {
        private BigDecimal principal;
        private double annualRate;
        private int tenureMonths;

        public BigDecimal getPrincipal() { return principal; }
        public void setPrincipal(BigDecimal principal) { this.principal = principal; }
        public double getAnnualRate() { return annualRate; }
        public void setAnnualRate(double annualRate) { this.annualRate = annualRate; }
        public int getTenureMonths() { return tenureMonths; }
        public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }
    }

    public static class EligibilityRequest {
        private Long userId;
        private BigDecimal amount;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }
}
