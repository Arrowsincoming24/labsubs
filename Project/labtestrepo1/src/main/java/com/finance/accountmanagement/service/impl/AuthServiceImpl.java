package com.finance.accountmanagement.service.impl;

import com.finance.accountmanagement.dto.request.LoginRequest;
import com.finance.accountmanagement.dto.request.RegisterRequest;
import com.finance.accountmanagement.dto.response.AuthResponse;
import com.finance.accountmanagement.entity.ChildAccount;
import com.finance.accountmanagement.entity.ParentAccount;
import com.finance.accountmanagement.repository.ChildAccountRepository;
import com.finance.accountmanagement.repository.ParentAccountRepository;
import com.finance.accountmanagement.security.JwtTokenUtil;
import com.finance.accountmanagement.security.UserDetailsImpl;
import com.finance.accountmanagement.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final ParentAccountRepository parentAccountRepository;
    private final ChildAccountRepository childAccountRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                         JwtTokenUtil jwtTokenUtil,
                         PasswordEncoder passwordEncoder,
                         ParentAccountRepository parentAccountRepository,
                         ChildAccountRepository childAccountRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.parentAccountRepository = parentAccountRepository;
        this.childAccountRepository = childAccountRepository;
    }

    @Override
    public AuthResponse registerParent(RegisterRequest request) {
        if (parentAccountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        ParentAccount parentAccount = new ParentAccount();
        parentAccount.setAccountHolderName(request.getAccountHolderName());
        parentAccount.setEmail(request.getEmail());
        parentAccount.setPassword(passwordEncoder.encode(request.getPassword()));
        parentAccount.setPhoneNumber(request.getPhoneNumber());
        parentAccount.setAddress(request.getAddress());
        parentAccount.setOccupation(request.getOccupation());
        parentAccount.setTaxId(request.getTaxId());
        parentAccount.setPreferredLanguage(request.getPreferredLanguage());
        parentAccount.setAccountNumber(generateAccountNumber());
        parentAccount.setSystemGeneratedBy("SYSTEM");
        parentAccount.setCreatedBy("SYSTEM");
        parentAccount.setUpdatedBy("SYSTEM");

        parentAccount = parentAccountRepository.save(parentAccount);
        String token = jwtTokenUtil.generateToken(new UserDetailsImpl(parentAccount));

        return AuthResponse.builder()
                .token(token)
                .accountType("PARENT")
                .accountNumber(parentAccount.getAccountNumber())
                .accountHolderName(parentAccount.getAccountHolderName())
                .email(parentAccount.getEmail())
                .build();
    }

    @Override
    public AuthResponse registerChild(RegisterRequest request, String parentEmail) {
        if (childAccountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        ParentAccount parentAccount = parentAccountRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new RuntimeException("Parent account not found"));

        ChildAccount childAccount = new ChildAccount();
        childAccount.setAccountHolderName(request.getAccountHolderName());
        childAccount.setEmail(request.getEmail());
        childAccount.setPassword(passwordEncoder.encode(request.getPassword()));
        childAccount.setParentAccount(parentAccount);
        childAccount.setAccountNumber(generateAccountNumber());
        childAccount.setSystemGeneratedBy("SYSTEM");
        childAccount.setCreatedBy("SYSTEM");
        childAccount.setUpdatedBy("SYSTEM");

        childAccount = childAccountRepository.save(childAccount);
        String token = jwtTokenUtil.generateToken(new UserDetailsImpl(childAccount));

        return AuthResponse.builder()
                .token(token)
                .accountType("CHILD")
                .accountNumber(childAccount.getAccountNumber())
                .accountHolderName(childAccount.getAccountHolderName())
                .email(childAccount.getEmail())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetailsImpl userDetails = parentAccountRepository.findByEmail(request.getEmail())
                .map(parent -> new UserDetailsImpl(parent))
                .orElseGet(() -> childAccountRepository.findByEmail(request.getEmail())
                        .map(child -> new UserDetailsImpl(child))
                        .orElseThrow(() -> new RuntimeException("Invalid credentials")));

        String token = jwtTokenUtil.generateToken(userDetails);
        String accountType = userDetails instanceof UserDetailsImpl ? 
                ((UserDetailsImpl) userDetails).getAccountType() : "UNKNOWN";

        return AuthResponse.builder()
                .token(token)
                .accountType(accountType)
                .email(userDetails.getUsername())
                .build();
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 