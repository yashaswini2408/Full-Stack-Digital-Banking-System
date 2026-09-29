package com.maverickbank.controller;

import com.maverickbank.entity.LoanType;
import com.maverickbank.service.LoanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/api/loan-types")
public class LoanTypeController {
    
    @Autowired
    private LoanTypeService loanTypeService;

    @Operation(summary = "Get all active loan types", description = "Returns all available loan types for customers")
    @GetMapping
    public ResponseEntity<List<LoanType>> getAllActiveLoanTypes() {
        return ResponseEntity.ok(loanTypeService.getAllActiveLoanTypes());
    }

    @Operation(summary = "Get loan type by ID", description = "Returns a specific loan type by ID")
    @GetMapping("/{id}")
    public ResponseEntity<LoanType> getLoanTypeById(@PathVariable Long id) {
        return loanTypeService.getLoanTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new loan type", description = "Create a new loan type (Admin only)")
    @PostMapping
    public ResponseEntity<LoanType> createLoanType(@RequestBody LoanType loanType) {
        return ResponseEntity.ok(loanTypeService.saveLoanType(loanType));
    }

    @Operation(summary = "Update loan type", description = "Update an existing loan type (Admin only)")
    @PutMapping("/{id}")
    public ResponseEntity<LoanType> updateLoanType(@PathVariable Long id, @RequestBody LoanType loanType) {
        loanType.setId(id);
        return ResponseEntity.ok(loanTypeService.saveLoanType(loanType));
    }

    @Operation(summary = "Delete loan type", description = "Deactivate a loan type (Admin only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoanType(@PathVariable Long id) {
        try {
            loanTypeService.deleteLoanType(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all loan types", description = "Get all loan types including inactive ones (Admin only)")
    @GetMapping("/all")
    public ResponseEntity<List<LoanType>> getAllLoanTypes() {
        return ResponseEntity.ok(loanTypeService.getAllLoanTypes());
    }
}
