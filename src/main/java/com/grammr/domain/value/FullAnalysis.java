package com.grammr.domain.value;

import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.Token;
import java.util.List;
import lombok.Builder;

@Builder
public record FullAnalysis(
    String sourcePhrase,
    SemanticTranslation semanticTranslation,
    List<Token> tokens
) {

}
