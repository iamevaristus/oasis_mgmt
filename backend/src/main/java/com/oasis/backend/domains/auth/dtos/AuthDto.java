package com.oasis.backend.domains.auth.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthDto extends BaseAuthDto implements Serializable {
    private UUID id;
}