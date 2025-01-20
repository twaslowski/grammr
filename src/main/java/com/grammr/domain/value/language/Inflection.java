package com.grammr.domain.value.language;

import com.grammr.domain.enums.features.FeatureProperty;
import java.util.Set;

public record Inflection(
    String lemma,
    String inflectedForm,
    Set<FeatureProperty> properties
) {

  /**
   * Represents the inflected form of a word. For instance, "gehst" would have the following properties:
   * lemma: gehen
   * inflectedForm: gehst
   * properties: Number: Sing, Person: Second, Tense: Present
   */

}
