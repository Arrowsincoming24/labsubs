package com.finance.accountmanagement.repository;

import com.finance.accountmanagement.entity.ParentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParentAccountRepository extends JpaRepository<ParentAccount, UUID> {
    Optional<ParentAccount> findByEmail(String email);
    Optional<ParentAccount> findByAccountNumber(String accountNumber);
    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
} 