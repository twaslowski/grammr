package com.grammr.port.dto;

import jakarta.validation.constraints.NotNull;

public record UserRegistrationRequest(
    @NotNull String username,
    @NotNull String password
) {

}
