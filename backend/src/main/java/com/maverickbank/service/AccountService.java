package com.maverickbank.service;

import com.maverickbank.entity.Account;
import com.maverickbank.entity.User;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.repository.AccountRepository;
import com.maverickbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Optional<Account> getAccountByNumber(String accountNumber) {
        return Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber));
    }

    public Account openAccount(Long userId, String accountType, String ifscCode, String branchName, String branchAddress, BigDecimal initialBalance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(accountType);
        account.setIfscCode(ifscCode);
        account.setBranchName(branchName);
        account.setBranchAddress(branchAddress);
        account.setBalance(initialBalance);
        account.setUser(user);
        return accountRepository.save(account);
    }

    private String generateAccountNumber() {
        return "ACCT" + System.currentTimeMillis();
    }
}
