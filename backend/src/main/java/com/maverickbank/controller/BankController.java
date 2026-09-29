package com.maverickbank.controller;

import com.maverickbank.entity.Bank;
import com.maverickbank.entity.Branch;
import com.maverickbank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@RequestMapping("/api/banks")
public class BankController {
    
    @Autowired
    private BankService bankService;

    @Operation(summary = "Get all banks", description = "Returns list of all banks")
    @GetMapping
    public ResponseEntity<List<Bank>> getAllBanks() {
        return ResponseEntity.ok(bankService.getAllBanks());
    }

    @Operation(summary = "Get bank by ID", description = "Returns a bank by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Bank> getBankById(
            @Parameter(name = "id", description = "Bank ID", required = true)
            @PathVariable Long id) {
        return bankService.getBankById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get branches by bank name", description = "Returns all branches for a specific bank")
    @GetMapping("/{bankName}/branches")
    public ResponseEntity<List<Branch>> getBranchesByBankName(
            @Parameter(name = "bankName", description = "Bank Name", required = true)
            @PathVariable String bankName) {
        return ResponseEntity.ok(bankService.getBranchesByBankName(bankName));
    }

    @Operation(summary = "Get branch by IFSC code", description = "Returns branch details by IFSC code")
    @GetMapping("/branches/ifsc/{ifscCode}")
    public ResponseEntity<Branch> getBranchByIfsc(
            @Parameter(name = "ifscCode", description = "IFSC Code", required = true)
            @PathVariable String ifscCode) {
        return bankService.getBranchByIfsc(ifscCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all branches", description = "Returns list of all branches")
    @GetMapping("/branches")
    public ResponseEntity<List<Branch>> getAllBranches() {
        return ResponseEntity.ok(bankService.getAllBranches());
    }

    @Operation(summary = "Create new bank", description = "Creates a new bank (Admin only)")
    @PostMapping
    public ResponseEntity<Bank> createBank(@RequestBody Bank bank) {
        return ResponseEntity.ok(bankService.saveBank(bank));
    }

    @Operation(summary = "Create new branch", description = "Creates a new branch (Admin only)")
    @PostMapping("/branches")
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
        return ResponseEntity.ok(bankService.saveBranch(branch));
    }
}
