package com.grammr.domain.entity;

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
@Table(name = "chat_message")
public class ChatMessage {

  public enum Role {
    SYSTEM,
    USER,
    ASSISTANT,
    DEVELOPER;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_message_id_seq")
  private Long id;

  @NotNull
  private UUID messageId;

  private String content;

  @Enumerated(EnumType.STRING)
  private Role role;

  @JoinColumn(name = "chat_id", nullable = false)
  @ManyToOne(fetch = FetchType.EAGER)
  private Chat chat;

  private Long tokenUsage;

  private UUID analysisId;

  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdTimestamp;

  @UpdateTimestamp
  private ZonedDateTime updatedTimestamp;

  public static ChatMessage from(String input, Chat chat, Role role, Long tokens) {
    return ChatMessage.builder()
        .content(input)
        .messageId(UUID.randomUUID())
        .chat(chat)
        .role(role)
        .tokenUsage(tokens)
        .build();
  }
}
