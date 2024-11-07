package com.grammr.domain.value.language;

import lombok.Builder;

@Builder
public record TranslatedToken(
    String source,
    String translation
) {

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    TranslatedToken that = (TranslatedToken) obj;
    return source.equalsIgnoreCase(that.source) && translation.equalsIgnoreCase(that.translation);
  }
}
