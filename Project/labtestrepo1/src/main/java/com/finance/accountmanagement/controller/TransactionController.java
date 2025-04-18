package com.finance.accountmanagement.controller;

import com.finance.accountmanagement.exception.InsufficientFundsException;
import com.finance.accountmanagement.exception.InvalidAmountException;
import com.finance.accountmanagement.exception.UnauthorizedAccessException;
import com.finance.accountmanagement.security.UserDetailsImpl;
import com.finance.accountmanagement.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/parent-to-child")
    public ResponseEntity<?> transferParentToChild(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam UUID childId,
            @RequestParam BigDecimal amount) {
        
        if (!"PARENT".equals(userDetails.getAccountType())) {
            return ResponseEntity.badRequest().body("Only parent accounts can perform this operation");
        }

        try {
            transactionService.transferParentToChild(userDetails.getUsername(), childId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (InvalidAmountException | InsufficientFundsException | UnauthorizedAccessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/child-to-parent")
    public ResponseEntity<?> transferChildToParent(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam BigDecimal amount) {
        
        if (!"CHILD".equals(userDetails.getAccountType())) {
            return ResponseEntity.badRequest().body("Only child accounts can perform this operation");
        }

        try {
            transactionService.transferChildToParent(userDetails.getUsername(), amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (InvalidAmountException | InsufficientFundsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam BigDecimal amount) {
        
        try {
            transactionService.deposit(userDetails.getUsername(), userDetails.getAccountType(), amount);
            return ResponseEntity.ok("Deposit successful");
        } catch (InvalidAmountException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam BigDecimal amount) {
        
        try {
            transactionService.withdraw(userDetails.getUsername(), userDetails.getAccountType(), amount);
            return ResponseEntity.ok("Withdrawal successful");
        } catch (InvalidAmountException | InsufficientFundsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}