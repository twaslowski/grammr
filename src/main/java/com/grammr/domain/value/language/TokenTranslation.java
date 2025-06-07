package com.grammr.domain.value.language;

import com.grammr.common.AIGeneratedContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenTranslation extends AIGeneratedContent {

  // The index maps to a Token
  private long index;
  private String source;
  private String translation;
  private boolean ambiguous;

  public TokenTranslation(String source, String translation) {
    this.source = source;
    this.translation = translation;
    this.ambiguous = false;
  }

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
