package com.oasis.backend.domains.account.service;

import com.oasis.backend.domains.account.dto.AccountDto;
import com.oasis.backend.domains.auth.responses.AuthResponse;
import com.oasis.backend.models.bases.ApiResponse;

/**
 * Interface defining the account service.
 */
public interface AccountService {

    /**
     * Updates the user's account information.
     *
     * @param account The updated account information.
     * @return An ApiResponse containing the authentication response.
     */
    ApiResponse<AuthResponse> update(AccountDto account);
}