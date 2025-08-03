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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

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

    // Export statuses
    CREATION_INITIATED,
    CREATION_SUCCEEDED,
    CREATION_FAILED,

    // Update statuses
    UPDATE_INITIATED,
    UPDATE_SUCCEEDED,
    UPDATE_FAILED,

    // Deletion statuses
    DELETION_INITIATED,
    DELETION_SUCCEEDED,
    DELETION_FAILED;
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
    this.status = switch (this.status) {
      case CREATION_INITIATED -> Status.CREATION_SUCCEEDED;
      case UPDATE_INITIATED -> Status.UPDATE_SUCCEEDED;
      case DELETION_INITIATED -> Status.DELETION_SUCCEEDED;
      default -> throw new IllegalStateException("Cannot confirm sync for flashcard with status: " + this.status);
    };
  }

  public void failSync() {
    this.status = switch (this.status) {
      case CREATION_INITIATED -> Status.CREATION_FAILED;
      case UPDATE_INITIATED -> Status.UPDATE_FAILED;
      case DELETION_INITIATED -> Status.DELETION_FAILED;
      default -> throw new IllegalStateException("Cannot fail sync for flashcard with status: " + this.status);
    };
  }

  public void initiateSync() {
    this.status = switch (this.status) {
      case CREATED -> Status.CREATION_INITIATED;
      case UPDATED -> Status.UPDATE_INITIATED;
      case MARKED_FOR_DELETION -> Status.DELETION_INITIATED;
      default -> throw new IllegalStateException("Cannot initiate sync for flashcard with status: " + this.status);
    };
  }

  public Flashcard updateWith(FlashcardCreationDto data) {
    this.front = data.question();
    this.back = data.answer();
    // Leaving updating Paradigm and tokenPos for the future
    this.status = Status.UPDATED;
    return this;
  }
}
