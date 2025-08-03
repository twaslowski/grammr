package com.grammr.flashcards.controller.v2.dto;

import java.util.List;
import java.util.UUID;

public record SyncResultDto(
    List<UUID> successfulSyncs,
    List<UUID> failedSyncs
) {
}
