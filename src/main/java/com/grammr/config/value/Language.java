package com.grammr.config.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grammr.domain.enums.LanguageCode;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record Language(
    LanguageCode code,
    MorphologyConfiguration morphology,
    InflectionConfiguration inflection
) {

}
