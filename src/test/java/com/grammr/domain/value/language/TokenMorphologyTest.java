package com.grammr.domain.value.language;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.domain.enums.features.Case;
import com.grammr.domain.enums.features.FeatureType;
import org.junit.jupiter.api.Test;

public class TokenMorphologyTest {

  @Test
  void shouldFindExistingFeature() {
    var morphology = TokenMorphologySpec.valid().build();

    assertThat(morphology.getFeature(FeatureType.CASE)).isPresent();
    assertThat(morphology.getFeature(FeatureType.CASE).orElseThrow()).isEqualTo(Case.NOM);
    assertThat(morphology.getFeature(FeatureType.CASE).orElseThrow()).isEqualTo(Case.NOM);
    assertThat(morphology.getFullFeatureIdentifier(FeatureType.CASE).orElseThrow()).isEqualTo("Nominative");
  }

  @Test
  void shouldNotFindNonExistingFeature() {
    var morphology = TokenMorphologySpec.valid().build();

    assertThat(morphology.getFeature(FeatureType.NUMBER)).isEmpty();
    assertThat(morphology.getFullFeatureIdentifier(FeatureType.NUMBER)).isEmpty();
  }
}
