package com.maverickbank.controller;

import com.maverickbank.entity.Account;
import com.maverickbank.entity.Transaction;
import com.maverickbank.entity.Loan;
import com.maverickbank.repository.AccountRepository;
import com.maverickbank.repository.TransactionRepository;
import com.maverickbank.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountRepository.findAll());
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanRepository.findAll());
    }

    @GetMapping("/total-balance")
    public ResponseEntity<BigDecimal> getTotalBalance() {
        return ResponseEntity.ok(accountRepository.findAll().stream().map(Account::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
