package com.grammr.port.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserRegistrationRequest(
    String username,
    @NotNull String email,
    @NotNull String password
) {

}
