package com.maverickbank.repository;

import com.maverickbank.entity.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {
    List<LoanType> findByIsActiveTrue();
}
