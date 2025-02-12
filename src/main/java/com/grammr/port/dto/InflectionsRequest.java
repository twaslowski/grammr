package com.grammr.port.dto;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.Token;

public record InflectionsRequest(LanguageCode languageCode, Token token) {

}
