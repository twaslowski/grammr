package com.grammr.domain.entity;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.value.language.Inflection;
import java.util.List;
import java.util.Set;

public class ParadigmSpec {

  public static Paradigm.ParadigmBuilder valid() {
    return Paradigm.builder()
        .lemma("run")
        .partOfSpeech(PartOfSpeechTag.VERB)
        .languageCode(LanguageCode.EN)
        .inflections(List.of(
            Inflection.builder().lemma("run").inflected("runs").features(Set.of()).build(),
            Inflection.builder().lemma("run").inflected("ran").features(Set.of()).build(),
            Inflection.builder().lemma("run").inflected("is running").features(Set.of()).build()
        ));
  }
}
