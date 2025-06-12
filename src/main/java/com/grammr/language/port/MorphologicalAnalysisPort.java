package com.grammr.language.port;

import com.grammr.config.value.LanguageConfiguration;
import com.grammr.domain.value.language.MorphologicalAnalysis;
import com.grammr.language.controller.v1.dto.MorphologicalAnalysisRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Slf4j
@Service
public class MorphologicalAnalysisPort {

  private final RestClient restClient;
  private final LanguageConfiguration languageConfiguration;

  public MorphologicalAnalysis performAnalysis(MorphologicalAnalysisRequest analysisRequest) {
    var uri = languageConfiguration.getMorphologyUri(analysisRequest.languageCode());
    log.info("Retrieving morphology for phrase '{}'", analysisRequest.phrase());
    try {
      return restClient
          .post()
          .uri(uri)
          .body(analysisRequest)
          .retrieve()
          .body(MorphologicalAnalysis.class);
    } catch (Exception e) {
      log.error("Error performing analysis for phrase: {}", analysisRequest.phrase(), e);
      throw e;
    }
  }
}
