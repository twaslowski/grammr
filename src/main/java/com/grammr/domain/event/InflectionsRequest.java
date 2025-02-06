package com.grammr.domain.event;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.Token;

public record InflectionsRequest(LanguageCode languageCode, Token token) {

}
