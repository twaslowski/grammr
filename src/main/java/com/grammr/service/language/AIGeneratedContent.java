package com.grammr.service.language;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public abstract class AIGeneratedContent {

  protected long completionTokens;
  protected long promptTokens;
}
