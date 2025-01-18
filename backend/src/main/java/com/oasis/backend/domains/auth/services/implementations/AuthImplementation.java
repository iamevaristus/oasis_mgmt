package com.oasis.backend.domains.auth.services.implementations;

import com.oasis.backend.configurations.exceptions.OasisException;
import com.oasis.backend.core.mappers.UserMapper;
import com.oasis.backend.core.session.SessionService;
import com.oasis.backend.domains.auth.dtos.LoginDto;
import com.oasis.backend.domains.auth.dtos.SignupDto;
import com.oasis.backend.domains.auth.responses.AuthResponse;
import com.oasis.backend.domains.auth.services.AuthService;
import com.oasis.backend.models.User;
import com.oasis.backend.models.bases.ApiResponse;
import com.oasis.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthImplementation implements AuthService {
    private final SessionService sessionService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<AuthResponse> login(LoginDto login) {
        User user = userRepository.findByEmailAddressIgnoreCase(login.getEmailAddress())
                .orElseThrow(() -> new OasisException("User not found"));

        if(passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            return sessionService.generateSession(UserMapper.instance.toAuthDto(user));
        } else {
            throw new OasisException("Incorrect user details. Check your email address and password");
        }
    }

    @Override
    public ApiResponse<AuthResponse> signup(SignupDto signup) {
        User existing = userRepository.findByEmailAddressIgnoreCase(signup.getEmailAddress()).orElse(null);

        if(existing != null) {
            throw new OasisException("User already exists");
        } else {
            User user = UserMapper.instance.toUser(signup);
            user.setPassword(passwordEncoder.encode(signup.getPassword()));
            userRepository.save(user);

            return sessionService.generateSession(UserMapper.instance.toAuthDto(user));
        }
    }
}