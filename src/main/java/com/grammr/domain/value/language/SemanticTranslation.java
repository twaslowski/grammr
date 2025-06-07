package com.grammr.domain.value.language;

import com.grammr.common.AIGeneratedContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SemanticTranslation extends AIGeneratedContent {

  private String sourcePhrase;
  private String translatedPhrase;
}
