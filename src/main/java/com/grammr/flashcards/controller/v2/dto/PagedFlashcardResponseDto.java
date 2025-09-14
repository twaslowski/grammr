package com.grammr.flashcards.controller.v2.dto;

import com.grammr.domain.entity.Flashcard;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PagedFlashcardResponseDto(
    List<FlashcardDto> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last,
    boolean hasNext,
    boolean hasPrevious
) {

  public static PagedFlashcardResponseDto fromPage(Page<Flashcard> page) {
    return PagedFlashcardResponseDto.builder()
        .content(page.getContent().stream().map(FlashcardDto::fromEntity).toList())
        .page(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .first(page.isFirst())
        .last(page.isLast())
        .hasNext(page.hasNext())
        .hasPrevious(page.hasPrevious())
        .build();
  }
}
