package com.grammr.domain.value;

import java.util.Collection;

public record LiteralTranslation(
    String sourcePhrase,
    Collection<TranslatedToken> translatedTokens
) {

}
