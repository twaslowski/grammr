package com.grammr.domain.value.language;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.domain.enums.features.Case;
import com.grammr.domain.enums.features.Feature;
import com.grammr.domain.enums.features.FeatureType;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TokenMorphologyTest {

  @Test
  void shouldFindExistingFeature() {
    var morphology = TokenMorphologySpec.valid()
        .features(List.of(Feature.of(FeatureType.CASE, "NOM")))
        .build();

    assertThat(morphology.getFeature(FeatureType.CASE).orElseThrow()).isEqualTo(Case.NOM);
    assertThat(morphology.getFullFeatureIdentifier(FeatureType.CASE)).isEqualTo("Nominative");

    assertThat(morphology.getFeature(FeatureType.NUMBER)).isEmpty();
    assertThat(morphology.getFullFeatureIdentifier(FeatureType.NUMBER)).isEmpty();
  }

  @Test
  void shouldNotFindNonExistingFeature() {
    var morphology = TokenMorphologySpec.valid()
        .features(List.of(Feature.of(FeatureType.CASE, "NOM")))
        .build();

    assertThat(morphology.getFeature(FeatureType.NUMBER)).isEmpty();
    assertThat(morphology.getFullFeatureIdentifier(FeatureType.NUMBER)).isEmpty();
  }
}
