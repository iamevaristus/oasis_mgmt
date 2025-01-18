package com.oasis.backend.core.session;

import com.oasis.backend.domains.auth.dtos.AuthDto;
import com.oasis.backend.domains.auth.responses.AuthResponse;
import com.oasis.backend.models.bases.ApiResponse;

/**
 * Service interface for managing user sessions.
 * <p>
 * This interface provides methods for generating, revoking, and validating user sessions,
 * along with handling refresh tokens and updating session details. Implementations of this
 * interface should handle the necessary business logic for session management in the application.
 * </p>
 *
 * @see SessionImplementation
 */
public interface SessionService {
    /**
     * Generates a new session based on the provided request.
     * <p>
     * This method creates a new user session by processing the session request data.
     * It returns an API response containing the authentication details, such as access tokens
     * and user information, allowing the user to access protected resources.
     * </p>
     *
     * @param auth The auth data containing user credentials and other necessary information.
     * @return ApiResponse containing the session response, including the generated tokens and user details.
     *
     * @see AuthResponse
     * @see ApiResponse
     */
    ApiResponse<AuthResponse> generateSession(AuthDto auth);

    /**
     * Validates the authenticity and expiration of a session token.
     * <p>
     * This method checks if the provided session token is valid, ensuring it has not expired
     * and is associated with an active session. It returns an API response indicating whether
     * the session is valid, along with any relevant status messages.
     * </p>
     *
     * @param token The session token to validate.
     *
     * @return ApiResponse indicating the validation status and any associated messages.
     *
     * @see ApiResponse
     */
    ApiResponse<String> validateSession(String token);

    /**
     * Signs out the user by revoking all sessions and refresh tokens.
     * <p>
     * This method logs the user out by invalidating all active sessions and refresh tokens
     * associated with the user's account. It ensures that the user can no longer access
     * protected resources until they log in again.
     * </p>
     */
    void signOut();
}