package com.grammr.chat.controller.dto;

import com.grammr.domain.enums.LanguageCode;

public record ChatInitializationDto(
    LanguageCode languageCode
) {

}
