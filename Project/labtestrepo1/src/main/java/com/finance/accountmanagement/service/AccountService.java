package com.finance.accountmanagement.service;

import com.finance.accountmanagement.entity.ChildAccount;
import com.finance.accountmanagement.entity.ParentAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    ParentAccount getParentAccountById(UUID id);
    ChildAccount getChildAccountById(UUID id);
    List<ChildAccount> getChildAccountsByParentId(UUID parentId);
    void deposit(UUID accountId, BigDecimal amount, boolean isParentAccount);
    void withdraw(UUID accountId, BigDecimal amount, boolean isParentAccount);
    void transferFromParentToChild(UUID parentId, UUID childId, BigDecimal amount);
    void transferFromChildToParent(UUID childId, UUID parentId, BigDecimal amount);
    Optional<ParentAccount> getCurrentParentAccount();
    Optional<ChildAccount> getCurrentChildAccount();
    List<ChildAccount> getCurrentParentChildAccounts();
} 