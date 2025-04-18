package com.finance.accountmanagement.security;

import com.finance.accountmanagement.repository.ChildAccountRepository;
import com.finance.accountmanagement.repository.ParentAccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final ParentAccountRepository parentAccountRepository;
    private final ChildAccountRepository childAccountRepository;

    public CustomUserDetailsService(ParentAccountRepository parentAccountRepository,
                                  ChildAccountRepository childAccountRepository) {
        this.parentAccountRepository = parentAccountRepository;
        this.childAccountRepository = childAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return parentAccountRepository.findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseGet(() -> childAccountRepository.findByEmail(email)
                        .map(UserDetailsImpl::new)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email)));
    }
} 