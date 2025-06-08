package com.grammr.chat.controller.v1.dto;

import com.grammr.chat.value.Message;
import com.grammr.domain.enums.LanguageCode;

public record ChatInitializationDto(
    LanguageCode languageCode,
    Message message
) {

}
