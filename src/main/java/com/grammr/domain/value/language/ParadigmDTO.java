package com.grammr.domain.value.language;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ParadigmDTO(
    @NotNull
    @Schema(
        description = "Unique paradigm identifier for later retrieval",
        example = "123e4567-e89b-12d3-a456-426614174000"
    )
    String paradigmId,
    @NotNull
    @Schema(
        description = "Part of speech for the paradigm",
        example = "VERB"
    )
    PartOfSpeechTag partOfSpeech,
    @NotNull
    @Schema(
        description = "Lemma of the inflected word",
        example = "laufen"
    )
    String lemma,
    @NotNull
    @Schema(
        description = "Language code for the paradigm",
        example = "DE"
    )
    LanguageCode language,
    @NotNull
    @Schema(
        description = "List of inflected forms for the paradigm"
    )
    List<Inflection> inflections) {

}
