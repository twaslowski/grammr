package com.grammr.domain.value.language;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.service.language.AIGeneratedContent;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class LanguageRecognition extends AIGeneratedContent {

  LanguageCode languageCode;
}
