package com.grammr.language.controller.v2;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.exception.BadRequestException;
import com.grammr.language.controller.v2.dto.WordTranslationRequest;
import com.grammr.language.service.v2.translation.word.WordTranslationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class TranslationControllerTest {

  @Mock
  WordTranslationService wordTranslationService;

  @InjectMocks
  TranslationController translationController;

  @Test
  void testTranslateWord_withContext_callsTranslate() {
    WordTranslationRequest request = new WordTranslationRequest(
        "word",
        true,
        "some context",
        LanguageCode.EN
    );

    translationController.translateWord(request);
    Mockito.verify(wordTranslationService).translate(request.source(), request.context(), request.targetLanguage());
    Mockito.verify(wordTranslationService, Mockito.never()).translateWithoutContext(ArgumentMatchers.anyString(), ArgumentMatchers.any(LanguageCode.class));
  }

  @Test
  void testTranslateWord_withoutContext_callsTranslateWithoutContext() {
    WordTranslationRequest request = new WordTranslationRequest(
        "word",
        false,
        "some context",
        LanguageCode.EN
    );

    translationController.translateWord(request);

    Mockito.verify(wordTranslationService).translateWithoutContext(request.source(), LanguageCode.EN);
    Mockito.verify(wordTranslationService, Mockito.never()).translate(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any(LanguageCode.class));
  }

  @Test
  void testTranslateWord_withContext_withoutContextProvided_throwsException() {
    WordTranslationRequest request = new WordTranslationRequest(
        "word",
        true,
        null,
        LanguageCode.EN
    );

    assertThatThrownBy(() -> translationController.translateWord(request))
        .isInstanceOf(BadRequestException.class);
  }
}