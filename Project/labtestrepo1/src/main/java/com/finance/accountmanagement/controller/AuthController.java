package com.finance.accountmanagement.controller;

import com.finance.accountmanagement.dto.request.LoginRequest;
import com.finance.accountmanagement.dto.request.RegisterRequest;
import com.finance.accountmanagement.dto.response.AuthResponse;
import com.finance.accountmanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/parent/register")
    public ResponseEntity<AuthResponse> registerParent(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerParent(request));
    }

    @PostMapping("/child/register")
    public ResponseEntity<AuthResponse> registerChild(@Valid @RequestBody RegisterRequest request,
                                                    @RequestParam String parentEmail) {
        return ResponseEntity.ok(authService.registerChild(request, parentEmail));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
} 