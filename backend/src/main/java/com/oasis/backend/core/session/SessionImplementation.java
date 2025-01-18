package com.oasis.backend.core.session;

import com.oasis.backend.configurations.exceptions.ExceptionCodes;
import com.oasis.backend.configurations.exceptions.OasisException;
import com.oasis.backend.core.jwt.JwtService;
import com.oasis.backend.core.mappers.UserMapper;
import com.oasis.backend.domains.auth.dtos.AuthDto;
import com.oasis.backend.domains.auth.responses.AuthResponse;
import com.oasis.backend.models.Session;
import com.oasis.backend.models.User;
import com.oasis.backend.models.bases.ApiResponse;
import com.oasis.backend.repositories.SessionRepository;
import com.oasis.backend.repositories.UserRepository;
import com.oasis.backend.utils.TimeUtil;
import com.oasis.backend.utils.UserUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service responsible for managing user sessions.
 * It implements its wrapper class {@link SessionService}
 *
 * @see SessionRepository
 * @see JwtService
 * @see UserRepository
 */
@Service
@RequiredArgsConstructor
public class SessionImplementation implements SessionService {
    private final SessionRepository sessionRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<AuthResponse> generateSession(AuthDto auth) {
        User user = userRepository.findById(auth.getId())
                .orElseThrow(() -> new OasisException("User not found"));

        revokeSessions(user.getId());
        generateAndFetchSessionId(auth, user);

        AuthResponse response = UserMapper.instance.toAuthResponse(user);
        response.setAccessToken(jwtService.generateToken(auth));

        return new ApiResponse<>(response);
    }

    private void revokeSessions(UUID userId) {
        List<Session> sessions = sessionRepository.findAllUserNonRevoked(userId);

        if(!sessions.isEmpty()) {
            sessions.forEach(session -> {
                if(!session.getRevoked()) {
                    session.setRevoked(true);
                    session.setUpdatedAt(TimeUtil.now());
                    sessionRepository.save(session);
                }
            });
        }
    }

    private void generateAndFetchSessionId(AuthDto auth, User user) {
        Session newSession = new Session();
        newSession.setUser(user);
        Session session = sessionRepository.save(newSession);

        auth.setId(session.getId());
    }

    @Override
    public ApiResponse<String> validateSession(String token) {
        try {
            if(jwtService.isTokenExpired(token)) {
                throw new OasisException("Your session has expired. Please login", ExceptionCodes.INVALID_SESSION);
            }

            String email = jwtService.getEmailFromToken(token);
            try {
                UUID sessionId = UUID.fromString(jwtService.getItemFromToken(token, "session"));

                Session session = sessionRepository.findById(sessionId)
                        .orElseThrow(() -> new OasisException("Invalid token"));
                User user = userRepository.findBySessions_Id(sessionId).orElseThrow(() -> new OasisException("User not found"));

                if(user.getEmailAddress().equals(email) && jwtService.isTokenIssuedByOasis(token) && !session.getRevoked()) {
                    return new ApiResponse<>("Token is valid", email, HttpStatus.OK);
                } else {
                    return new ApiResponse<>("Invalid token");
                }
            } catch (IllegalArgumentException e) {
                throw new OasisException("Invalid session. Please login", ExceptionCodes.INVALID_SESSION);
            }
        } catch (ExpiredJwtException e) {
            return new ApiResponse<>("Token has expired. Please login");
        } catch (MalformedJwtException e) {
            return new ApiResponse<>("Invalid token. Please login to continue");
        } catch (Exception e) {
            return new ApiResponse<>("Invalid token. Please verify your token or login again");
        }
    }

    @Override
    public void signOut() {
        try {
            var user = userRepository.findByEmailAddressIgnoreCase(UserUtil.getLoggedInUser())
                    .orElseThrow(() -> new OasisException("User not found"));

            revokeSessions(user.getId());
        } catch (IllegalArgumentException e) {
            throw new OasisException("Invalid session. Please login", ExceptionCodes.INVALID_SESSION);
        }
    }
}