package com.grammr.service.language.translation.literal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.grammr.language.service.translation.literal.OpenAILiteralTranslationService;
import com.grammr.language.service.translation.literal.OpenAITokenTranslationService;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.language.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OpenAILiteralTranslationServiceTest {

  @InjectMocks
  private OpenAILiteralTranslationService openAILiteralTranslationService;

  @Mock
  private OpenAITokenTranslationService openAITokenTranslationService;

  private final TokenService tokenService = new TokenService();

  @Test
  void shouldFilterOutPunctuation() {
    var phrase = "Hello, world!";
    var tokens = tokenService.tokenize(phrase);

    var request = AnalysisComponentRequest.builder()
        .phrase(phrase)
        .targetLanguage(LanguageCode.DE)
        .sourceLanguage(LanguageCode.EN)
        .tokens(tokens)
        .build();

    openAILiteralTranslationService.createAnalysisComponent(request);

    verify(openAITokenTranslationService).createTranslation(phrase, tokens.get(0), LanguageCode.DE);
    verify(openAITokenTranslationService).createTranslation(phrase, tokens.get(2), LanguageCode.DE);
    verify(openAITokenTranslationService, times(2)).createTranslation(any(), any(), any());
  }
}