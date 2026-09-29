package com.maverickbank.repository;

import com.maverickbank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);
    Account findByAccountNumber(String accountNumber);
}
