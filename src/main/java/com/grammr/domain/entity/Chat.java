package com.grammr.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.springframework.lang.Nullable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat")
public class Chat {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_id_seq")
  private Long id;

  @NotNull
  private UUID chatId;

  @Nullable
  @ManyToOne
  @JoinColumn(name = "owner")
  private User owner;

  private String summary;

  private Long totalTokens;

  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdTimestamp;

  @UpdateTimestamp
  private ZonedDateTime updatedTimestamp;

  public void incrementTokens(Long tokens) {
    if (this.totalTokens == null) {
      this.totalTokens = tokens;
    }
    this.totalTokens += tokens;
  }
}
