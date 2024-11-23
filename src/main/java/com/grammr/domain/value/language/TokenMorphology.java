package com.grammr.domain.value.language;

import lombok.Builder;

@Builder
public record TokenMorphology(
    String text,
    String lemma
) {

}
