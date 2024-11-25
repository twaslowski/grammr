package com.grammr.domain.enums.features;

import java.util.Set;

public interface FeatureProperty {

  String fullIdentifier();

  FeatureType type();

  Set<FeatureCategory> categories();
}
