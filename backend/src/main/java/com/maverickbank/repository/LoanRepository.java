package com.maverickbank.repository;

import com.maverickbank.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);
    List<Loan> findByStatus(String status);
    List<Loan> findByStatusOrderByApplicationDateDesc(String status);
    List<Loan> findByUserIdAndStatus(Long userId, String status);
}
