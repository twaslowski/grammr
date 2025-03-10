package com.grammr.config.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record InflectionConfiguration(
    boolean enabled,
    String uri
) {

}
