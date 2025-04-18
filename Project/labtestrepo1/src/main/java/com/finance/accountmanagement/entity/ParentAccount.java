package com.finance.accountmanagement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parent_accounts")
public class ParentAccount extends BaseEntity {
    @Column(nullable = false)
    private String accountHolderName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String occupation;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private String taxId;

    @Column(nullable = false)
    private String preferredLanguage;

    @OneToMany(mappedBy = "parentAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildAccount> childAccounts = new ArrayList<>();

    public ParentAccount() {
    }

    public ParentAccount(String accountHolderName, String email, String password, String phoneNumber,
                        String address, String occupation, BigDecimal balance, Boolean active,
                        List<ChildAccount> childAccounts) {
        this.accountHolderName = accountHolderName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.occupation = occupation;
        this.balance = balance;
        this.active = active;
        this.childAccounts = childAccounts;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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

    public List<ChildAccount> getChildAccounts() {
        return childAccounts;
    }

    public void setChildAccounts(List<ChildAccount> childAccounts) {
        this.childAccounts = childAccounts;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public Boolean getIsActive() {
        return active;
    }

    public void setIsActive(Boolean active) {
        this.active = active;
    }
} 