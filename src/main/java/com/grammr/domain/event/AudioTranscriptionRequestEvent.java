package com.grammr.domain.event;

import com.grammr.domain.enums.LanguageCode;
import java.nio.file.Path;
import lombok.Builder;

@Builder
public record AudioTranscriptionRequestEvent(
    Path path,
    LanguageCode userLanguageLearned,
    LanguageCode userLanguageSpoken,
    String requestId
) {

}
