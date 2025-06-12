package com.grammr.language.service.v2;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.Token;
import com.grammr.language.service.MorphologyService;
import com.grammr.language.service.v1.TokenService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("v2AnalysisService")
@RequiredArgsConstructor
public class AnalysisService {

  private final TokenService tokenService;
  private final MorphologyService morphologyService;

  public List<Token> simpleAnalyze(String phrase, LanguageCode languageCode) {
    var tokens = tokenService.tokenize(phrase);

    var analysis = morphologyService.performAnalysis(phrase, languageCode);
    return tokenService.enrichTokens(tokens, List.of(), analysis);
  }
}
