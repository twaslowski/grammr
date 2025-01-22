package com.grammr.domain.exception;

import com.grammr.domain.enums.features.FeatureProperty;
import java.util.Set;

public class InflectionNotFoundException extends RuntimeException {

  public InflectionNotFoundException(String lemma, Set<FeatureProperty> properties) {
    super("Inflection not found for lemma: " + lemma + " and features: " + properties);
  }
}
