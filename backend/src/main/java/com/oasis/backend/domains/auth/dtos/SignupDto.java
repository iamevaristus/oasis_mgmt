package com.oasis.backend.domains.auth.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignupDto extends BaseAuthDto implements Serializable {
    private String password;
}