package com.finance.accountmanagement.service;

import com.finance.accountmanagement.entity.ChildAccount;
import com.finance.accountmanagement.entity.ParentAccount;
import com.finance.accountmanagement.exception.InsufficientFundsException;
import com.finance.accountmanagement.exception.InvalidAmountException;
import com.finance.accountmanagement.exception.UnauthorizedAccessException;
import com.finance.accountmanagement.repository.ChildAccountRepository;
import com.finance.accountmanagement.repository.ParentAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionService {

    private final ParentAccountRepository parentAccountRepository;
    private final ChildAccountRepository childAccountRepository;

    public TransactionService(ParentAccountRepository parentAccountRepository,
                            ChildAccountRepository childAccountRepository) {
        this.parentAccountRepository = parentAccountRepository;
        this.childAccountRepository = childAccountRepository;
    }

    @Transactional
    public void transferParentToChild(String parentEmail, UUID childId, BigDecimal amount) {
        validateAmount(amount);

        ParentAccount parentAccount = parentAccountRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Parent account not found"));

        ChildAccount childAccount = childAccountRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("Child account not found"));

        if (!childAccount.getParentAccount().getId().equals(parentAccount.getId())) {
            throw new UnauthorizedAccessException("Child account does not belong to this parent");
        }

        if (parentAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in parent account");
        }

        parentAccount.setBalance(parentAccount.getBalance().subtract(amount));
        childAccount.setBalance(childAccount.getBalance().add(amount));

        parentAccountRepository.save(parentAccount);
        childAccountRepository.save(childAccount);
    }

    @Transactional
    public void transferChildToParent(String childEmail, BigDecimal amount) {
        validateAmount(amount);

        ChildAccount childAccount = childAccountRepository.findByEmail(childEmail)
                .orElseThrow(() -> new IllegalArgumentException("Child account not found"));

        if (childAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in child account");
        }

        ParentAccount parentAccount = childAccount.getParentAccount();
        childAccount.setBalance(childAccount.getBalance().subtract(amount));
        parentAccount.setBalance(parentAccount.getBalance().add(amount));

        childAccountRepository.save(childAccount);
        parentAccountRepository.save(parentAccount);
    }

    @Transactional
    public void deposit(String email, String accountType, BigDecimal amount) {
        validateAmount(amount);

        if ("PARENT".equals(accountType)) {
            ParentAccount parentAccount = parentAccountRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Parent account not found"));
            parentAccount.setBalance(parentAccount.getBalance().add(amount));
            parentAccountRepository.save(parentAccount);
        } else {
            ChildAccount childAccount = childAccountRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Child account not found"));
            childAccount.setBalance(childAccount.getBalance().add(amount));
            childAccountRepository.save(childAccount);
        }
    }

    @Transactional
    public void withdraw(String email, String accountType, BigDecimal amount) {
        validateAmount(amount);

        if ("PARENT".equals(accountType)) {
            ParentAccount parentAccount = parentAccountRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Parent account not found"));
            if (parentAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in parent account");
            }
            parentAccount.setBalance(parentAccount.getBalance().subtract(amount));
            parentAccountRepository.save(parentAccount);
        } else {
            ChildAccount childAccount = childAccountRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Child account not found"));
            if (childAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in child account");
            }
            childAccount.setBalance(childAccount.getBalance().subtract(amount));
            childAccountRepository.save(childAccount);
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
    }
}