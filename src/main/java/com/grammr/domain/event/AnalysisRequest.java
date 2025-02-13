package com.grammr.domain.event;

import com.grammr.domain.enums.LanguageCode;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.lang.Nullable;

@Builder
public record AnalysisRequest(
    String requestId,
    @NotNull String phrase,
    @NotNull LanguageCode userLanguageSpoken,
    @NotNull LanguageCode userLanguageLearned,
    @Nullable boolean performSemanticTranslation
) {

}
