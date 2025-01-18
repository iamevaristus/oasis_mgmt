package com.oasis.backend.utils;

import com.oasis.backend.configurations.exceptions.OasisException;
import com.oasis.backend.models.User;
import com.oasis.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * The UserUtil class provides utility methods related to user authentication and retrieval.
 */
@Service
@RequiredArgsConstructor
public class UserUtil {
    private final UserRepository userRepository;

    /**
     * Retrieves the username of the currently logged-in user.
     * @return The username of the logged-in user.
     */
    public static String getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    /**
     * Retrieves the user entity corresponding to the currently logged-in user.
     * @return The user entity.
     */
    public User getUser() {
        return userRepository.findByEmailAddressIgnoreCase(getLoggedInUser())
                .orElseThrow(() -> new OasisException("User not found"));
    }
}
