package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.enums.features.Case;
import com.grammr.domain.enums.features.FeatureType;
import com.grammr.domain.enums.features.Number;
import com.grammr.service.language.morphology.MorphologicalAnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@IntegrationTest
@SpringBootTest
public class MorphologicalAnalysisIntegrationTest {

  @Autowired
  private MorphologicalAnalysisService analysisService;

  @Test
  void shouldCreateAnalysis() {
    var phrase = "как у тебя дела?";
    var analysis = analysisService.analyze(phrase);

    assertThat(analysis).isNotNull();
    assertThat(analysis.requestId()).isNotNull();
    assertThat(analysis.tokens().size()).isEqualTo(4);

    var tebya = analysis.findByText("тебя").orElseThrow();
    assertThat(tebya.partOfSpeechTag()).isEqualTo(PartOfSpeechTag.PRON);

    var dela = analysis.findByText("дела").orElseThrow();
    assertThat(dela.partOfSpeechTag()).isEqualTo(PartOfSpeechTag.NOUN);
    assertThat(tebya.getFeature(FeatureType.CASE).orElseThrow()).isEqualTo(Case.NOM);
    assertThat(tebya.getFeature(FeatureType.NUMBER).orElseThrow()).isEqualTo(Number.PLUR);
  }
}
