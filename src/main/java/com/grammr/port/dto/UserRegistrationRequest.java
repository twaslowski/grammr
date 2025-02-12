package com.grammr.port.dto;

import jakarta.validation.constraints.NotNull;

public record UserRegistrationRequest(
    @NotNull String email,
    @NotNull String password
) {

}
