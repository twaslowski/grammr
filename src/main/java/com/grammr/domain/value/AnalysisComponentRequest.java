package com.grammr.domain.value;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.AnalysisRequest;
import com.grammr.domain.value.language.Token;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@Builder
public class AnalysisComponentRequest {

  @Nullable
  private String phrase;

  @Nullable
  private LanguageCode sourceLanguage;

  @Nullable
  private LanguageCode targetLanguage;

  @Nullable
  private List<Token> tokens;

  public static AnalysisComponentRequest from(AnalysisRequest analysisRequest) {
    return AnalysisComponentRequest.builder()
        .phrase(analysisRequest.phrase())
        .sourceLanguage(analysisRequest.userLanguageLearned())
        .targetLanguage(analysisRequest.userLanguageSpoken())
        .tokens(List.of())
        .build();
  }

  public static AnalysisComponentRequest from(AnalysisRequest analysisRequest, List<Token> tokens) {
    return AnalysisComponentRequest.builder()
        .phrase(analysisRequest.phrase())
        .sourceLanguage(analysisRequest.userLanguageSpoken())
        .targetLanguage(analysisRequest.userLanguageLearned())
        .tokens(tokens)
        .build();
  }
}
