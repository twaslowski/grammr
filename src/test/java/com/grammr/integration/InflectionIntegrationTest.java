package com.grammr.integration;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenMorphology;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class InflectionIntegrationTest extends IntegrationTestBase {

  @Test
  void shouldRetrieveInflectionsForWord() {
    String lemma = "дело";
    var morphology = TokenMorphology.builder().lemma(lemma).partOfSpeechTag(PartOfSpeechTag.NOUN).build();
    var token = Token.builder()
        .text("дела")
        .morphology(morphology)
        .build();

    // when
    var result = inflectionPort.performInflections(LanguageCode.RU, token);
    assertThat(result).isNotNull();
  }
}
