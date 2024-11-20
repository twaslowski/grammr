package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.annotation.IntegrationTest;
import com.grammr.language.analysis.GrammaticalAnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@IntegrationTest
@SpringBootTest
public class GrammaticalAnalysisIntegrationTest {

  @Autowired
  private GrammaticalAnalysisService analysisService;

  @Test
  void shouldCreateAnalysis() {
    var phrase = "как у тебя дела?";
    var analysis = analysisService.analyze(phrase);

    assertThat(analysis).isNotNull();
  }
}
