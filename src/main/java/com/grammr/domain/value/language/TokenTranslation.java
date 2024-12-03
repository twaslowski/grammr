package com.grammr.domain.value.language;

import com.grammr.service.language.AIGeneratedContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenTranslation extends AIGeneratedContent {

  private String source;
  private String translation;

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
