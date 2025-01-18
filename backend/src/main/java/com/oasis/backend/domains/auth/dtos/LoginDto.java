package com.oasis.backend.domains.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginDto {
    @JsonProperty("email_address")
    private String emailAddress;

    private String password;
}