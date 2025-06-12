package com.grammr.language.controller.v2.dto;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.Token;
import java.util.List;

public record PhraseAnalysis(
    String phrase,
    LanguageCode languageCode,
    List<Token> tokens
) {

}
