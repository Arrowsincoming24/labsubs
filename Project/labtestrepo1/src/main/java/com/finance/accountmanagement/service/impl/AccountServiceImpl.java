package com.finance.accountmanagement.service.impl;

import com.finance.accountmanagement.entity.ChildAccount;
import com.finance.accountmanagement.entity.ParentAccount;
import com.finance.accountmanagement.repository.ChildAccountRepository;
import com.finance.accountmanagement.repository.ParentAccountRepository;
import com.finance.accountmanagement.security.UserDetailsImpl;
import com.finance.accountmanagement.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final ParentAccountRepository parentAccountRepository;
    private final ChildAccountRepository childAccountRepository;

    public AccountServiceImpl(ParentAccountRepository parentAccountRepository,
                            ChildAccountRepository childAccountRepository) {
        this.parentAccountRepository = parentAccountRepository;
        this.childAccountRepository = childAccountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ParentAccount getParentAccountById(UUID id) {
        return parentAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parent account not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public ChildAccount getChildAccountById(UUID id) {
        return childAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Child account not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChildAccount> getChildAccountsByParentId(UUID parentId) {
        ParentAccount parentAccount = getParentAccountById(parentId);
        return childAccountRepository.findByParentAccount(parentAccount);
    }

    @Override
    @Transactional
    public void deposit(UUID accountId, BigDecimal amount, boolean isParentAccount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        if (isParentAccount) {
            ParentAccount account = getParentAccountById(accountId);
            account.setBalance(account.getBalance().add(amount));
            parentAccountRepository.save(account);
        } else {
            ChildAccount account = getChildAccountById(accountId);
            account.setBalance(account.getBalance().add(amount));
            childAccountRepository.save(account);
        }
    }

    @Override
    @Transactional
    public void withdraw(UUID accountId, BigDecimal amount, boolean isParentAccount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        if (isParentAccount) {
            ParentAccount account = getParentAccountById(accountId);
            if (account.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Insufficient funds in parent account");
            }
            account.setBalance(account.getBalance().subtract(amount));
            parentAccountRepository.save(account);
        } else {
            ChildAccount account = getChildAccountById(accountId);
            if (account.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Insufficient funds in child account");
            }
            account.setBalance(account.getBalance().subtract(amount));
            childAccountRepository.save(account);
        }
    }

    @Override
    @Transactional
    public void transferFromParentToChild(UUID parentId, UUID childId, BigDecimal amount) {
        ParentAccount parentAccount = getParentAccountById(parentId);
        ChildAccount childAccount = getChildAccountById(childId);

        if (!childAccount.getParentAccount().getId().equals(parentId)) {
            throw new IllegalStateException("Child account does not belong to this parent");
        }

        withdraw(parentId, amount, true);
        deposit(childId, amount, false);
    }

    @Override
    @Transactional
    public void transferFromChildToParent(UUID childId, UUID parentId, BigDecimal amount) {
        ChildAccount childAccount = getChildAccountById(childId);
        ParentAccount parentAccount = getParentAccountById(parentId);

        if (!childAccount.getParentAccount().getId().equals(parentId)) {
            throw new IllegalStateException("Child account does not belong to this parent");
        }

        withdraw(childId, amount, false);
        deposit(parentId, amount, true);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParentAccount> getCurrentParentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            if ("PARENT".equals(userDetails.getAccountType())) {
                return parentAccountRepository.findByEmail(userDetails.getUsername());
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChildAccount> getCurrentChildAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            if ("CHILD".equals(userDetails.getAccountType())) {
                return childAccountRepository.findByEmail(userDetails.getUsername());
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChildAccount> getCurrentParentChildAccounts() {
        return getCurrentParentAccount()
                .map(parent -> childAccountRepository.findByParentAccount(parent))
                .orElseThrow(() -> new IllegalStateException("Current user is not a parent account"));
    }
} 