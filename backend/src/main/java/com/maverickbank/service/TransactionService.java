package com.maverickbank.service;

import com.maverickbank.entity.Transaction;
import com.maverickbank.entity.Account;
import com.maverickbank.entity.Beneficiary;
import com.maverickbank.repository.TransactionRepository;
import com.maverickbank.repository.AccountRepository;
import com.maverickbank.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountIdOrderByDateDesc(accountId);
    }

    public List<Transaction> getRecentTransactionsByAccountId(Long accountId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return transactionRepository.findByAccountIdOrderByDateDesc(accountId, pageable);
    }

    public List<Transaction> getTransactionsByAccountIdAndDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByAccountIdAndDateBetweenOrderByDateDesc(accountId, startDate, endDate);
    }

    public List<Transaction> getRecentTransactionsByUserId(Long userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return transactionRepository.findByUserIdOrderByDateDesc(userId, pageable);
    }

    public List<Transaction> getTransactionsByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    public List<Transaction> getMonthlyTransactionsByUserId(Long userId, int month, int year) {
        LocalDateTime startDate = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);
        return getTransactionsByUserIdAndDateRange(userId, startDate, endDate);
    }

    public Transaction deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        
        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        
        Transaction tx = new Transaction();
        tx.setType("DEPOSIT");
        tx.setAmount(amount);
        tx.setDate(LocalDateTime.now());
        tx.setAccount(account);
        tx.setDescription("Deposit");
        return transactionRepository.save(tx);
    }

    public Transaction withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        
        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        
        Transaction tx = new Transaction();
        tx.setType("WITHDRAWAL");
        tx.setAmount(amount);
        tx.setDate(LocalDateTime.now());
        tx.setAccount(account);
        tx.setDescription("Withdrawal");
        return transactionRepository.save(tx);
    }

    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> new RuntimeException("Source account not found"));
        Account toAccount = accountRepository.findById(toAccountId).orElseThrow(() -> new RuntimeException("Destination account not found"));
        
        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        
        // Perform transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        
        // Create transaction records for both accounts
        Transaction debitTx = new Transaction();
        debitTx.setType("TRANSFER_DEBIT");
        debitTx.setAmount(amount);
        debitTx.setDate(LocalDateTime.now());
        debitTx.setAccount(fromAccount);
        debitTx.setDescription("Transfer to account " + toAccount.getAccountNumber());
        transactionRepository.save(debitTx);
        
        Transaction creditTx = new Transaction();
        creditTx.setType("TRANSFER_CREDIT");
        creditTx.setAmount(amount);
        creditTx.setDate(LocalDateTime.now());
        creditTx.setAccount(toAccount);
        creditTx.setDescription("Transfer from account " + fromAccount.getAccountNumber());
        transactionRepository.save(creditTx);
        
        return debitTx;
    }

    public Transaction transferToBeneficiary(Long fromAccountId, Long beneficiaryId, BigDecimal amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> new RuntimeException("Source account not found"));
        
        // Get beneficiary details
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId).orElseThrow(() -> new RuntimeException("Beneficiary not found"));
        
        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        
        // Deduct from source account
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);
        
        // Create transaction record
        Transaction debitTx = new Transaction();
        debitTx.setType("BENEFICIARY_TRANSFER");
        debitTx.setAmount(amount);
        debitTx.setDate(LocalDateTime.now());
        debitTx.setAccount(fromAccount);
        debitTx.setDescription(description != null ? description : "Transfer to " + beneficiary.getName() + " (" + beneficiary.getAccountNumber() + ")");
        
        return transactionRepository.save(debitTx);
    }

    public Transaction transferByAccountNumber(Long fromAccountId, String toAccountNumber, BigDecimal amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> new RuntimeException("Source account not found"));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);
        
        if (toAccount == null) {
            throw new RuntimeException("Destination account not found");
        }
        
        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        
        // Perform transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        
        // Create transaction records for both accounts
        Transaction debitTx = new Transaction();
        debitTx.setType("TRANSFER_DEBIT");
        debitTx.setAmount(amount);
        debitTx.setDate(LocalDateTime.now());
        debitTx.setAccount(fromAccount);
        debitTx.setDescription(description != null ? description : "Transfer to account " + toAccount.getAccountNumber());
        transactionRepository.save(debitTx);
        
        Transaction creditTx = new Transaction();
        creditTx.setType("TRANSFER_CREDIT");
        creditTx.setAmount(amount);
        creditTx.setDate(LocalDateTime.now());
        creditTx.setAccount(toAccount);
        creditTx.setDescription(description != null ? description : "Transfer from account " + fromAccount.getAccountNumber());
        transactionRepository.save(creditTx);
        
        return debitTx;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
