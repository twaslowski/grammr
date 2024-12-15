package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.enums.features.FeatureType;
import com.grammr.domain.enums.features.Number;
import com.grammr.domain.enums.features.Person;
import com.grammr.domain.enums.features.Tense;
import com.grammr.service.language.morphology.MorphologicalAnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class MorphologicalAnalysisIntegrationTest extends IntegrationTestBase {

  @Autowired
  private MorphologicalAnalysisService analysisService;

  @Test
  void shouldCreateAnalysis() {
    var phrase = "Ich lerne Deutsch.";
    var analysis = analysisService.analyze(phrase, LanguageCode.DE);

    assertThat(analysis).isNotNull();
    assertThat(analysis.requestId()).isNotNull();
    assertThat(analysis.tokens().size()).isEqualTo(3);

    var ich = analysis.findByText("Ich").orElseThrow();
    assertThat(ich.partOfSpeechTag()).isEqualTo(PartOfSpeechTag.PRON);

    var lerne = analysis.findByText("lerne").orElseThrow();
    assertThat(lerne.partOfSpeechTag()).isEqualTo(PartOfSpeechTag.VERB);
    assertThat(lerne.getFeature(FeatureType.PERSON).orElseThrow()).isEqualTo(Person.FIRST);
    assertThat(lerne.getFeature(FeatureType.NUMBER).orElseThrow()).isEqualTo(Number.SING);
    assertThat(lerne.getFeature(FeatureType.TENSE).orElseThrow()).isEqualTo(Tense.PRES);

    var deutsch = analysis.findByText("Deutsch").orElseThrow();
    assertThat(deutsch.partOfSpeechTag()).isEqualTo(PartOfSpeechTag.NOUN);
  }
}
