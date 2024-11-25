package com.grammr.domain.value.language;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.enums.features.Feature;
import com.grammr.domain.enums.features.FeatureProperty;
import com.grammr.domain.enums.features.FeatureType;
import java.util.List;
import java.util.Optional;
import lombok.Builder;

@Builder
public record TokenMorphology(
    String text,
    String lemma,
    @JsonProperty("pos")
    PartOfSpeechTag partOfSpeechTag,
    List<Feature> features
) {

  // If a Feature is available on a Token, return its value. Includes validation.
  public Optional<? extends Enum<? extends FeatureProperty>> getFeature(FeatureType featureType) {
    return features.stream()
        .filter(feature -> feature.type().equals(featureType))
        .map(Feature::getEnumValue)
        .findFirst();
  }

  public Optional<String> getFullFeatureIdentifier(FeatureType featureType) {
    return getFeature(featureType)
        .map(feature -> ((FeatureProperty) feature).fullIdentifier());
  }
}
