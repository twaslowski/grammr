package com.grammr.domain.value.language;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grammr.domain.enums.PartOfSpeechTag;
import lombok.Builder;

@Builder
public record TokenMorphology(
    String text,
    String lemma,
    @JsonProperty("pos")
    PartOfSpeechTag partOfSpeechTag
) {

}
