package com.finance.accountmanagement.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "child_accounts")
public class ChildAccount extends BaseEntity {

    @Column(nullable = false)
    private String accountHolderName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id", nullable = false, columnDefinition = "BINARY(16)")
    private ParentAccount parentAccount;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean active = true;

    public ChildAccount() {
    }

    public ChildAccount(String accountHolderName, String email, String password, BigDecimal balance,
                       Boolean active, ParentAccount parentAccount) {
        this.accountHolderName = accountHolderName;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.active = active;
        this.parentAccount = parentAccount;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ParentAccount getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(ParentAccount parentAccount) {
        this.parentAccount = parentAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Boolean getIsActive() {
        return active;
    }

    public void setIsActive(Boolean active) {
        this.active = active;
    }
} 