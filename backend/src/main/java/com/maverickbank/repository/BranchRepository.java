package com.maverickbank.repository;

import com.maverickbank.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByBankId(Long bankId);
    Optional<Branch> findByIfscCode(String ifscCode);
    List<Branch> findByBankBankName(String bankName);
}
