package com.grammr.config.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record MorphologyConfiguration(
    boolean enabled,
    String uri
) {

}
