package com.grammr.language.service.v2;

import com.grammr.domain.entity.Analysis;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.exception.ResourceNotFoundException;
import com.grammr.language.controller.v2.dto.PhraseAnalysis;
import com.grammr.language.service.MorphologyService;
import com.grammr.language.service.v1.TokenService;
import com.grammr.repository.AnalysisRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("v2AnalysisService")
@RequiredArgsConstructor
public class AnalysisService {

  private final TokenService tokenService;
  private final MorphologyService morphologyService;
  private final AnalysisRepository analysisRepository;

  public PhraseAnalysis performAnalysis(String phrase, LanguageCode languageCode) {
    var tokens = tokenService.tokenize(phrase);

    var morphologicalAnalysis = morphologyService.performAnalysis(phrase, languageCode);
    var analysedTokens = tokenService.enrichTokens(tokens, List.of(), morphologicalAnalysis);

    var analysis = analysisRepository.save(Analysis.from(phrase, languageCode, analysedTokens));
    return PhraseAnalysis.builder()
        .analysisId(analysis.getAnalysisId().toString())
        .phrase(analysis.getPhrase())
        .sourceLanguage(analysis.getSourceLanguage())
        .analysedTokens(analysedTokens)
        .build();
  }

  public PhraseAnalysis retrieveAnalysis(UUID analysisId) {
    log.info("Retrieving analysis for ID: {}", analysisId);
    var analysis = analysisRepository.findByAnalysisId(analysisId)
        .orElseThrow(() -> new ResourceNotFoundException(analysisId.toString()));

    return PhraseAnalysis.builder()
        .analysisId(analysis.getAnalysisId().toString())
        .phrase(analysis.getPhrase())
        .sourceLanguage(analysis.getSourceLanguage())
        .analysedTokens(analysis.getAnalysedTokens())
        .build();
  }
}
