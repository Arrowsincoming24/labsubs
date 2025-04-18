package com.finance.accountmanagement.repository;

import com.finance.accountmanagement.entity.ChildAccount;
import com.finance.accountmanagement.entity.ParentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChildAccountRepository extends JpaRepository<ChildAccount, UUID> {
    Optional<ChildAccount> findByEmail(String email);
    Optional<ChildAccount> findByAccountNumber(String accountNumber);
    List<ChildAccount> findByParentAccount(ParentAccount parentAccount);
    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
} 