package com.grammr.domain.value.language;

import lombok.Builder;

@Builder
public record TokenTranslation(String source, String translation) {

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
