package com.grammr.config;

import com.grammr.service.language.AnalysisComponentProvider;
import com.grammr.service.language.morphology.MorphologicalAnalysisService;
import com.grammr.service.language.translation.literal.LiteralTranslationService;
import com.grammr.service.language.translation.semantic.SemanticTranslationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class AnalysisComponentProviderConfiguration {

  @Bean
  List<AnalysisComponentProvider> analysisComponentProviders(SemanticTranslationService semanticTranslationService,
                                                             LiteralTranslationService literalTranslationService,
                                                             MorphologicalAnalysisService morphologicalAnalysisService) {
    return List.of(semanticTranslationService, literalTranslationService, morphologicalAnalysisService);
  }
}
