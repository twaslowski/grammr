package com.grammr.flashcards.controller.v2.dto;

import com.grammr.domain.entity.Paradigm;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.value.language.Inflection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ParadigmDto(
    @Schema(
        description = "Unique identifier for the paradigm",
        example = "123e4567-e89b-12d3-a456-426614174000"
    )
    String id,
    @Schema(
        description = "Part of speech tag for the paradigm",
        example = "NOUN"
    )
    PartOfSpeechTag partOfSpeechTag,
    @Schema(
        description = "The lemma of the paradigm",
        example = "cat"
    )
    String lemma,
    @Schema(
        description = "Language code for the paradigm",
        example = "en"
    )
    String languageCode,
    @Schema(
        description = "List of inflections associated with the paradigm"
    )
    List<Inflection> inflections
) {

  public static ParadigmDto fromEntity(Paradigm paradigm) {
    if (paradigm == null) {
      return  null;
    }
    return new ParadigmDto(
        paradigm.getId().toString(),
        paradigm.getPartOfSpeech(),
        paradigm.getLemma(),
        paradigm.getLanguageCode().toString(),
        paradigm.getInflections()
    );
  }
}
