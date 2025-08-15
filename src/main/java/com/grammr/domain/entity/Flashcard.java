package com.grammr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.flashcards.controller.v2.dto.FlashcardCreationDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flashcard")
public class Flashcard {

  public enum Status {
    CREATED,
    UPDATED,
    MARKED_FOR_DELETION,
    SYNCED,
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flashcard_id_seq")
  private long id;

  @NotNull
  private UUID flashcardId;

  @NotNull
  private String front;

  @NotNull
  private String back;

  @Enumerated(EnumType.STRING)
  private PartOfSpeechTag tokenPos;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "paradigm_id")
  private Paradigm paradigm;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  @JsonIgnore
  private Deck deck;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Status status;

  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdTimestamp;

  @UpdateTimestamp
  private ZonedDateTime updatedTimestamp;

  public void confirmSync() {
    this.status = Status.SYNCED;
  }

  public Flashcard updateWith(FlashcardCreationDto data) {
    this.front = data.question();
    this.back = data.answer();
    // Leaving updating Paradigm and tokenPos for the future
    this.status = Status.UPDATED;
    return this;
  }
}
