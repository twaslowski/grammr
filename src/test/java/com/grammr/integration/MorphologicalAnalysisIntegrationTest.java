package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.annotation.IntegrationTest;
import com.grammr.service.language.analysis.MorphologicalAnalysisService;
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
  }
}
