package com.oasis.backend.domains.auth.services;

import com.oasis.backend.domains.auth.dtos.LoginDto;
import com.oasis.backend.domains.auth.dtos.SignupDto;
import com.oasis.backend.domains.auth.responses.AuthResponse;
import com.oasis.backend.models.bases.ApiResponse;

/**
 * Interface defining the authentication service.
 */
public interface AuthService {

    /**
     * Logs in a user with the provided credentials.
     *
     * @param login The login credentials.
     * @return An ApiResponse containing the authentication response.
     */
    ApiResponse<AuthResponse> login(LoginDto login);

    /**
     * Registers a new user with the provided credentials.
     *
     * @param signup The user registration data.
     * @return An ApiResponse containing the authentication response.
     */
    ApiResponse<AuthResponse> signup(SignupDto signup);
}