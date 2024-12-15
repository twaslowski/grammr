package com.grammr.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
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
@Table(name = "request")
public class Request {

  public enum Status {
    PENDING,
    IN_PROGRESS,
    COMPLETED
  }

  @Id
  @GeneratedValue(generator = "request_id_seq")
  private long id;

  @NotNull
  private String requestId;

  private long chatId;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Status status;

  private int promptTokens;

  private int completionTokens;

  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdTimestamp;

  @UpdateTimestamp
  private ZonedDateTime updatedTimestamp;

  public static Request from(String requestId, long chatId) {
    return Request.builder()
        .requestId(requestId)
        .chatId(chatId)
        .status(Status.PENDING)
        .build();
  }
}
