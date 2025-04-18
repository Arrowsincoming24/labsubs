package com.finance.accountmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.accountmanagement.entity.ChildAccount;
import com.finance.accountmanagement.entity.ParentAccount;
import com.finance.accountmanagement.repository.ChildAccountRepository;
import com.finance.accountmanagement.repository.ParentAccountRepository;
import com.finance.accountmanagement.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final ParentAccountRepository parentAccountRepository;
    private final ChildAccountRepository childAccountRepository;

    public AccountController(ParentAccountRepository parentAccountRepository,
                           ChildAccountRepository childAccountRepository) {
        this.parentAccountRepository = parentAccountRepository;
        this.childAccountRepository = childAccountRepository;
    }

    @GetMapping("/parent/{id}")
    public ResponseEntity<ParentAccount> getParentAccount(@PathVariable UUID id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return parentAccountRepository.findById(id)
                .filter(parent -> userDetails.getUsername().equals(parent.getEmail()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/child/{id}")
    public ResponseEntity<ChildAccount> getChildAccount(@PathVariable UUID id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return childAccountRepository.findById(id)
                .filter(child -> {
                    if ("PARENT".equals(userDetails.getAccountType())) {
                        return parentAccountRepository.findByEmail(userDetails.getUsername())
                                .map(parent -> parent.getChildAccounts().contains(child))
                                .orElse(false);
                    } else {
                        return userDetails.getUsername().equals(child.getEmail());
                    }
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/parent/{id}/children")
    public ResponseEntity<List<ChildAccount>> getChildAccounts(@PathVariable UUID id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return parentAccountRepository.findById(id)
                .filter(parent -> userDetails.getUsername().equals(parent.getEmail()))
                .map(parent -> ResponseEntity.ok(parent.getChildAccounts()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentAccount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if ("PARENT".equals(userDetails.getAccountType())) {
            return parentAccountRepository.findByEmail(userDetails.getUsername())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return childAccountRepository.findByEmail(userDetails.getUsername())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
    }

    @GetMapping("/me/children")
    public ResponseEntity<List<ChildAccount>> getMyChildAccounts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!"PARENT".equals(userDetails.getAccountType())) {
            return ResponseEntity.badRequest().build();
        }
        return parentAccountRepository.findByEmail(userDetails.getUsername())
                .map(parent -> ResponseEntity.ok(parent.getChildAccounts()))
                .orElse(ResponseEntity.notFound().build());
    }
}