package com.grammr.language.controller.v1.dto;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InflectionsRequest(@NotNull LanguageCode languageCode,
                                 @NotNull String lemma,
                                 @NotNull PartOfSpeechTag partOfSpeechTag) {

}
