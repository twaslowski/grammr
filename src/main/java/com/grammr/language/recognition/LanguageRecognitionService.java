package com.grammr.language.recognition;

import com.grammr.domain.value.language.LanguageRecognition;

public interface LanguageRecognitionService {

  LanguageRecognition recognizeLanguage(String phrase);
}
