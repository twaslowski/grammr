package com.grammr.domain.value.language;

import com.grammr.domain.enums.features.Feature;
import java.util.Set;

/**
 * Represents the inflected form of a word. For instance, "gehst" would have the following features:
 * lemma: gehen
 * inflectedForm: gehst
 * features: Number: Sing, Person: Second, Tense: Present
 */

public record Inflection(
    String lemma,
    String inflected,
    Set<Feature> features
) {

}
