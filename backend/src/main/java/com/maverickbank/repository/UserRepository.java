package com.maverickbank.repository;

import com.maverickbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByIsActiveTrue();
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1")
    List<User> findByRoleName(String roleName);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1 AND u.isActive = true")
    List<User> findActiveUsersByRoleName(String roleName);
}
