package com.grammr.domain.value.language;

import com.grammr.domain.enums.AnalysisComponentType;
import com.grammr.service.language.AIGeneratedContent;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Builder
@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LiteralTranslation extends AIGeneratedContent {

  String sourcePhrase;
  List<TokenTranslation> tokenTranslations;

  @Override
  public AnalysisComponentType type() {
    return AnalysisComponentType.LITERAL_TRANSLATION;
  }
}
