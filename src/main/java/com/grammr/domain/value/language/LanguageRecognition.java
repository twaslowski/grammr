package com.grammr.domain.value.language;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.service.language.AIGeneratedContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LanguageRecognition extends AIGeneratedContent {

  private LanguageCode languageCode;

  public static LanguageRecognition of(LanguageCode languageCode) {
    return new LanguageRecognition(languageCode);
  }
}
