package com.grammr.domain.value.language;

import com.grammr.domain.enums.features.FeatureProperty;
import com.grammr.domain.exception.InflectionNotFoundException;
import java.util.List;
import java.util.Set;

public record Inflections(String lemma, List<Inflection> inflections) {

  public Inflection getBy(Set<FeatureProperty> featureProperties) {
    return inflections.stream()
        .filter(inflection -> inflection.features().equals(featureProperties))
        .findFirst()
        .orElseThrow(() -> new InflectionNotFoundException(lemma, featureProperties));
  }
}
