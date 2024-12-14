package com.grammr.domain.entity;

import com.grammr.domain.event.AnalysisRequestEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  enum Status {
    PENDING,
    IN_PROGRESS,
    COMPLETED
  }

  @Id
  private long id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

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

  public static Request from(AnalysisRequestEvent analysisRequestEvent) {
    return Request.builder()
        .user(analysisRequestEvent.user())
        .status(Status.PENDING)
        .build();
  }
}
