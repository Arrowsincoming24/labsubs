package com.finance.accountmanagement.dto.response;

public class AuthResponse {
    private String token;
    private String email;
    private String accountType;
    private String accountNumber;
    private String accountHolderName;

    public AuthResponse() {
    }

    public AuthResponse(String token, String email, String accountType, String accountNumber, String accountHolderName) {
        this.token = token;
        this.email = email;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String token;
        private String email;
        private String accountType;
        private String accountNumber;
        private String accountHolderName;

        private Builder() {
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder accountType(String accountType) {
            this.accountType = accountType;
            return this;
        }

        public Builder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder accountHolderName(String accountHolderName) {
            this.accountHolderName = accountHolderName;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(token, email, accountType, accountNumber, accountHolderName);
        }
    }
} 