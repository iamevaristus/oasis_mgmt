package com.oasis.backend.core.mappers;

import com.oasis.backend.domains.account.dto.AccountDto;
import com.oasis.backend.domains.auth.dtos.AuthDto;
import com.oasis.backend.domains.auth.dtos.SignupDto;
import com.oasis.backend.domains.auth.responses.AuthResponse;
import com.oasis.backend.models.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserMapper instance = Mappers.getMapper(UserMapper.class);

    AuthResponse toAuthResponse(User user);

    AuthDto toAuthDto(User user);

    @Mapping(source = "password", target = "password", ignore = true)
    User toUser(SignupDto signupDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(AccountDto account, @MappingTarget User user);
}