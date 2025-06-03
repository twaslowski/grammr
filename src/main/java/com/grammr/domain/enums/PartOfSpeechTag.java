package com.grammr.domain.enums;

import static com.grammr.domain.enums.features.FeatureCategory.NOMINAL;
import static com.grammr.domain.enums.features.FeatureCategory.OTHER;
import static com.grammr.domain.enums.features.FeatureCategory.VERBAL;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.grammr.domain.enums.features.FeatureCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PartOfSpeechTag {

  /**
   * Represents universal part-of-speech tags as defined by the
   * <a href="https://universaldependencies.org/u/pos/index.html">Universal Dependencies</a>
   * project.
   */

  // Open Class Words
  ADJ("Adjective", NOMINAL),
  PROPN("Proper Noun", NOMINAL),
  NOUN("Noun", NOMINAL),
  VERB("Verb", VERBAL),
  ADV("Adverb", OTHER),
  INTJ("Interjection", OTHER),

  // Closed Class Words
  ADP("Adposition", OTHER),
  AUX("Auxiliary Verb", VERBAL),
  CCONJ("Coordinating Conjunction", OTHER),
  DET("Determiner", NOMINAL),
  NUM("Numeral", OTHER),
  PART("Particle", OTHER),
  PRON("Pronoun", NOMINAL),
  SCONJ("Subordinating Conjunction", OTHER),

  // Other
  PUNCT("Punctuation", OTHER),
  SYM("Symbol", OTHER),

  @JsonEnumDefaultValue
  X("Other", OTHER);

  private final String fullIdentifier;
  private final FeatureCategory featureCategory;
}
