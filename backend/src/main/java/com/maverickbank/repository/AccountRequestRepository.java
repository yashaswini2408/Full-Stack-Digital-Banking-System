package com.maverickbank.repository;

import com.maverickbank.entity.AccountRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {
    List<AccountRequest> findByStatus(String status);
    List<AccountRequest> findByUserId(Long userId);
    List<AccountRequest> findByStatusOrderByRequestDateDesc(String status);
}
