package com.finance.accountmanagement.security;

import com.finance.accountmanagement.entity.ChildAccount;
import com.finance.accountmanagement.entity.ParentAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private final String email;
    private final String password;
    private final String accountType;
    private final boolean active;

    public UserDetailsImpl(ParentAccount parentAccount) {
        this.email = parentAccount.getEmail();
        this.password = parentAccount.getPassword();
        this.accountType = "PARENT";
        this.active = parentAccount.getIsActive();
    }

    public UserDetailsImpl(ChildAccount childAccount) {
        this.email = childAccount.getEmail();
        this.password = childAccount.getPassword();
        this.accountType = "CHILD";
        this.active = childAccount.getIsActive();
    }

    public String getAccountType() {
        return accountType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + accountType));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
} 