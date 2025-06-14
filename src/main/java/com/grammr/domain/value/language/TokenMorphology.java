package com.grammr.domain.value.language;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.enums.features.Feature;
import com.grammr.domain.enums.features.FeatureProperty;
import com.grammr.domain.enums.features.FeatureType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import lombok.Builder;

@Builder
public record TokenMorphology(
    @NotNull
    @Schema(
        description = "The text of the token",
        example = "goes",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String text,
    @NotNull
    @Schema(
        description = "The lemma of the token",
        example = "go",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String lemma,
    @NotNull
    @Schema(
        description = "The part of speech tag for the token",
        example = "VERB",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("pos")
    PartOfSpeechTag partOfSpeechTag,
    @NotNull
    @Schema(
        description = "A list of features associated with the token. May be empty.",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "[{\"type\": \"TENSE\", \"value\": \"PRES\", \"fullIdentifier\": \"Present Tense\"}]"
    )
    List<Feature> features
) {

  // If a Feature is available on a Token, return its value. Includes validation.
  public Optional<FeatureProperty> getFeature(FeatureType featureType) {
    return features.stream()
        .filter(feature -> feature.type().equals(featureType))
        .map(Feature::getEnumValue)
        .findFirst();
  }

  public String getFullFeatureIdentifier(FeatureType featureType) {
    return getFeature(featureType)
        .map(feature -> ((FeatureProperty) feature).fullIdentifier())
        .orElse("");
  }
}
