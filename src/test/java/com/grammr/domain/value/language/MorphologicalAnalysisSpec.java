package com.grammr.domain.value.language;

import java.util.List;

public class MorphologicalAnalysisSpec {

  public static MorphologicalAnalysis.MorphologicalAnalysisBuilder valid() {
    return MorphologicalAnalysis.builder()
        .sourcePhrase("some phrase")
        .requestId("some id")
        .tokens(List.of());
  }
}
