package com.grammr.chat.controller.v2.dto;

import com.grammr.domain.enums.LanguageCode;

public record ChatInitializationDto(
    LanguageCode languageCode,
    String messageContent
) {

}
