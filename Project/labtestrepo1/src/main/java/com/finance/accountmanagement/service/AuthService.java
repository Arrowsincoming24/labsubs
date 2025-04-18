package com.finance.accountmanagement.service;

import com.finance.accountmanagement.dto.request.LoginRequest;
import com.finance.accountmanagement.dto.request.RegisterRequest;
import com.finance.accountmanagement.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse registerParent(RegisterRequest request);
    AuthResponse registerChild(RegisterRequest request, String parentEmail);
    AuthResponse login(LoginRequest request);
} 