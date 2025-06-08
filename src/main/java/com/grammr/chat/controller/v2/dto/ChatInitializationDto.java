package com.grammr.chat.controller.v2.dto;

import com.grammr.domain.enums.LanguageCode;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChatInitializationDto(
    @NotNull LanguageCode languageCode,
    @NotNull String message
) {

}
