package com.grammr.domain.value.language;

import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.enums.features.Feature;
import com.grammr.domain.enums.features.FeatureType;
import java.util.List;

public class TokenMorphologySpec {

  public static TokenMorphology.TokenMorphologyBuilder valid() {
    return TokenMorphology.builder()
        .text("text")
        .lemma("lemma")
        .partOfSpeechTag(PartOfSpeechTag.NOUN)
        .features(List.of(
            Feature.of(FeatureType.CASE, "NOM"),
            Feature.of(FeatureType.NUMBER, "SING"),
            Feature.of(FeatureType.GENDER, "MASC")
        ));
  }
}
