package com.grammr.domain.value.language;

import com.grammr.service.language.AIGeneratedContent;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class TokenTranslation extends AIGeneratedContent {

  String source;
  String translation;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    TokenTranslation that = (TokenTranslation) obj;
    return source.equalsIgnoreCase(that.source) && translation.equalsIgnoreCase(that.translation);
  }
}
