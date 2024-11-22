package com.grammr.domain.value;

import com.grammr.domain.value.language.MorphologicalAnalysis;
import java.util.List;

public class MorphologicalAnalysisSpec {

  public static MorphologicalAnalysis.MorphologicalAnalysisBuilder valid() {
    return MorphologicalAnalysis.builder()
        .sourcePhrase("some phrase")
        .requestId("some id")
        .tokens(List.of());
  }
}
