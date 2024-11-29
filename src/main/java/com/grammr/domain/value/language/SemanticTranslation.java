package com.grammr.domain.value.language;

import com.grammr.service.language.AIGeneratedContent;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SemanticTranslation extends AIGeneratedContent {

  private final String sourcePhrase;
  private final String translatedPhrase;
}
