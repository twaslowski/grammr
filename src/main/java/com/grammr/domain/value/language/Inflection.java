package com.grammr.domain.value.language;

import com.grammr.domain.enums.features.Feature;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Builder;

/**
 * Represents the inflected form of a word. For instance, "gehst" would have the following features:
 * lemma: gehen
 * inflectedForm: gehst
 * features: Number: Sing, Person: Second, Tense: Present
 */

@Builder
public record Inflection(
    @NotNull
    @Schema(
        description = "Lemma of the inflected word",
        example = "laufen"
    )
    String lemma,
    @NotNull
    @Schema(
        description = "Inflected form of the word",
        example = "läufst"
    )
    String inflected,
    @NotNull
    @Schema(
        description = "Set of features associated with the inflected form",
        example = "[{\"type\": \"NUMBER\", \"value\": \"SING\", \"fullIdentifier\": \"Singular\"}, {\"type\": \"PERSON\", \"value\": \"SECOND\", \"fullIdentifier\": \"Second Person\"}, {\"type\": \"TENSE\", \"value\": \"PRES\", \"fullIdentifier\": \"Present Tense\"}]"
    )
    Set<Feature> features
) {

}
