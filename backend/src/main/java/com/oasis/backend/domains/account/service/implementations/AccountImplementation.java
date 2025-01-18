package com.oasis.backend.domains.account.service.implementations;

import com.oasis.backend.core.mappers.UserMapper;
import com.oasis.backend.core.session.SessionService;
import com.oasis.backend.domains.account.dto.AccountDto;
import com.oasis.backend.domains.account.service.AccountService;
import com.oasis.backend.domains.auth.responses.AuthResponse;
import com.oasis.backend.models.User;
import com.oasis.backend.models.bases.ApiResponse;
import com.oasis.backend.repositories.UserRepository;
import com.oasis.backend.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountImplementation implements AccountService {
    private final UserUtil userUtil;
    private final SessionService sessionService;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<AuthResponse> update(AccountDto account) {
        User user = userUtil.getUser();
        UserMapper.instance.updateUser(account, user);
        userRepository.save(user);

        return sessionService.generateSession(UserMapper.instance.toAuthDto(user));
    }
}
