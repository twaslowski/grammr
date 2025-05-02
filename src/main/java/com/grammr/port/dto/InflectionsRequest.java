package com.grammr.port.dto;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.value.language.Token;
import lombok.Builder;

@Builder
public record InflectionsRequest(LanguageCode languageCode,
                                 String lemma,
                                 PartOfSpeechTag partOfSpeechTag) {

}
