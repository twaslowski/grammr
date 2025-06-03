package com.grammr.controller.dto;

import com.grammr.domain.enums.LanguageCode;

public record ChatInitRequest(
    LanguageCode languageCode
) {

}
