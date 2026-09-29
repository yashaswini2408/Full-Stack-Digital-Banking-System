package com.maverickbank.repository;

import com.maverickbank.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
    Optional<Bank> findByBankName(String bankName);
    Optional<Bank> findByBankCode(String bankCode);
}
