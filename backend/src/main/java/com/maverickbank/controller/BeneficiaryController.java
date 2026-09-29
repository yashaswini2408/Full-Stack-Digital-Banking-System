package com.maverickbank.controller;

import com.maverickbank.entity.Beneficiary;
import com.maverickbank.service.BeneficiaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {
    @Autowired
    private BeneficiaryService beneficiaryService;


    @Operation(summary = "Get beneficiaries by user ID", description = "Returns all beneficiaries for a specific user.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Beneficiary>> getBeneficiariesByUser(
            @Parameter(name = "userId", description = "User ID", required = true, example = "1")
            @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(beneficiaryService.getBeneficiariesByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Beneficiary> addBeneficiary(@RequestBody BeneficiaryAddRequest request) {
        return ResponseEntity.ok(beneficiaryService.addBeneficiary(
            request.getUserId(),
            request.getName(),
            request.getAccountNumber(),
            request.getBankName(),
            request.getBranchName(),
            request.getIfscCode()
        ));
    }

    @Operation(summary = "Delete beneficiary", description = "Deletes a beneficiary by ID.")
    @DeleteMapping("/{beneficiaryId}")
    public ResponseEntity<Void> deleteBeneficiary(
            @Parameter(name = "beneficiaryId", description = "Beneficiary ID", required = true, example = "1")
            @PathVariable(name = "beneficiaryId") Long beneficiaryId) {
        beneficiaryService.deleteBeneficiary(beneficiaryId);
        return ResponseEntity.ok().build();
    }

    public static class BeneficiaryAddRequest {
        private Long userId;
        private String name;
        private String accountNumber;
        private String bankName;
        private String branchName;
        private String ifscCode;
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAccountNumber() { return accountNumber; }
        public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
        public String getBankName() { return bankName; }
        public void setBankName(String bankName) { this.bankName = bankName; }
        public String getBranchName() { return branchName; }
        public void setBranchName(String branchName) { this.branchName = branchName; }
        public String getIfscCode() { return ifscCode; }
        public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
    }
}
