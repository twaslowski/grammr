package com.grammr.service.language;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AIGeneratedContent {

  @JsonIgnore
  protected int completionTokens;

  @JsonIgnore
  protected int promptTokens;
}
