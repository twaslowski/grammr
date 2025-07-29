package com.grammr.flashcards.controller.v2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record SyncDto(
    @Schema(
        description = "Unique identifier for the sync operation",
        example = "ff45fa93-dbcf-4b85-9994-6dcc86bada5a",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    UUID syncId,
    @Schema(
        description = "Timestamp when the sync operation started",
        example = "2023-10-01T12:00:00Z",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    ZonedDateTime startedAt,
    @Schema(
        description = "List of flashcards to be synced",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    List<FlashcardDto> flashcards
) {

  public static SyncDto of(UUID syncId, List<FlashcardDto> flashcards) {
    return new SyncDto(
        syncId,
        ZonedDateTime.now(),
        flashcards
    );
  }
}
