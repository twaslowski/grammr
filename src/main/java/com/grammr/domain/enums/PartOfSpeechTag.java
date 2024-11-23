package com.grammr.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PartOfSpeechTag {

  /**
   * Represents universal part-of-speech tags as defined by the
   * <a href="https://universaldependencies.org/u/pos/index.html">Universal Dependencies</a>
   * project.
   */

  // Open Class Words
  ADJ("Adjective"),
  ADV("Adverb"),
  NOUN("Noun"),
  INTJ("Interjection"),
  PROPN("Proper Noun"),
  VERB("Verb"),

  // Closed Class Words
  ADP("Adposition"),
  AUX("Auxiliary Verb"),
  CCONJ("Coordinating Conjunction"),
  DET("Determiner"),
  NUM("Numeral"),
  PART("Particle"),
  PRON("Pronoun"),
  SCONJ("Subordinating Conjunction"),

  // Other
  PUNCT("Punctuation"),
  SYM("Symbol"),
  X("Other");

  private final String value;
}
