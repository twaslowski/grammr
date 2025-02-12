package com.grammr.port.dto;

public record UserRegistrationResponse(
    long userId,
    String sessionCookie
) {

}
