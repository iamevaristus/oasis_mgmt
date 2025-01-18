package com.oasis.backend.domains.auth.controllers;

import com.oasis.backend.domains.auth.dtos.LoginDto;
import com.oasis.backend.domains.auth.dtos.SignupDto;
import com.oasis.backend.domains.auth.responses.AuthResponse;
import com.oasis.backend.domains.auth.services.AuthService;
import com.oasis.backend.models.bases.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginDto login) {
        ApiResponse<AuthResponse> response = service.login(login);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@RequestBody SignupDto signup) {
        ApiResponse<AuthResponse> response = service.signup(signup);

        return new ResponseEntity<>(response, response.getStatus());
    }
}
