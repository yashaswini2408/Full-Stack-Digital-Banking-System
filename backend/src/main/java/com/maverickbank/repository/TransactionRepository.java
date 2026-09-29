package com.maverickbank.repository;

import com.maverickbank.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);
    
    List<Transaction> findByAccountIdOrderByDateDesc(Long accountId);
    
    List<Transaction> findByAccountIdOrderByDateDesc(Long accountId, Pageable pageable);
    
    List<Transaction> findByAccountIdAndDateBetweenOrderByDateDesc(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT t FROM Transaction t WHERE t.account.user.id = ?1 ORDER BY t.date DESC")
    List<Transaction> findByUserIdOrderByDateDesc(Long userId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.account.user.id = ?1 AND t.date BETWEEN ?2 AND ?3 ORDER BY t.date DESC")
    List<Transaction> findByUserIdAndDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
