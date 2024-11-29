package com.grammr.domain.value.language;

import java.util.Collection;
import lombok.Builder;

@Builder
public record LiteralTranslation(
    String sourcePhrase,
    Collection<TokenTranslation> tokenTranslations
) {

}
