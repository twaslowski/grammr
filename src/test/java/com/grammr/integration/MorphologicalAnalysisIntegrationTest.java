package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.PERIOD;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.enums.features.Animacy;
import com.grammr.domain.enums.features.Case;
import com.grammr.domain.enums.features.FeatureType;
import com.grammr.domain.enums.features.Gender;
import com.grammr.domain.enums.features.Number;
import com.grammr.domain.enums.features.Person;
import com.grammr.domain.enums.features.Tense;
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
    var phrase = "я люблю собаку.";
    var analysis = analysisService.analyze(phrase);

    assertThat(analysis).isNotNull();
    assertThat(analysis.requestId()).isNotNull();
    assertThat(analysis.tokens().size()).isEqualTo(3);

    var ya = analysis.findByText("я").orElseThrow();
    assertThat(ya.partOfSpeechTag()).isEqualTo(PartOfSpeechTag.PRON);

    var lyublyu = analysis.findByText("люблю").orElseThrow();
    assertThat(lyublyu.partOfSpeechTag()).isEqualTo(PartOfSpeechTag.VERB);
    assertThat(lyublyu.getFeature(FeatureType.PERSON).orElseThrow()).isEqualTo(Person.FIRST);
    assertThat(lyublyu.getFeature(FeatureType.NUMBER).orElseThrow()).isEqualTo(Number.SING);
    assertThat(lyublyu.getFeature(FeatureType.TENSE).orElseThrow()).isEqualTo(Tense.PRES);

    var sobaku = analysis.findByText("собаку").orElseThrow();
    assertThat(sobaku.partOfSpeechTag()).isEqualTo(PartOfSpeechTag.NOUN);
    assertThat(sobaku.getFeature(FeatureType.ANIMACY).orElseThrow()).isEqualTo(Animacy.ANIM);
    assertThat(sobaku.getFeature(FeatureType.ANIMACY).orElseThrow()).isEqualTo(Animacy.ANIM);
  }
}
