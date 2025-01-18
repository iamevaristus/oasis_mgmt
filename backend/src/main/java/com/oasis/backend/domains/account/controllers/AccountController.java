package com.oasis.backend.domains.account.controllers;

import com.oasis.backend.domains.account.dto.AccountDto;
import com.oasis.backend.domains.account.service.AccountService;
import com.oasis.backend.domains.auth.responses.AuthResponse;
import com.oasis.backend.models.bases.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService service;

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<AuthResponse>> update(@RequestBody AccountDto account) {
        ApiResponse<AuthResponse> response = service.update(account);
        return new ResponseEntity<>(response, response.getStatus());
    }
}