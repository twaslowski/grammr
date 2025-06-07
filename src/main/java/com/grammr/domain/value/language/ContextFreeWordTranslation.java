package com.grammr.domain.value.language;

import com.grammr.common.AIGeneratedContent;
import java.util.List;
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
public final class ContextFreeWordTranslation extends AIGeneratedContent {

  private String source;
  private List<WordTranslation> translations;
}
