package com.grammr.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grammr.domain.value.AnalysisComponent;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AIGeneratedContent extends AnalysisComponent {

  @JsonIgnore
  protected int completionTokens;

  @JsonIgnore
  protected int promptTokens;
}
