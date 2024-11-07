package com.grammr.domain.value.language;

import java.util.Collection;

public record LiteralTranslation(
    String sourcePhrase,
    Collection<TranslatedToken> translatedTokens
) {

}
