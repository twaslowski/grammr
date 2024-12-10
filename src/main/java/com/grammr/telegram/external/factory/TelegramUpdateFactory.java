package com.grammr.telegram.external.factory;

import com.grammr.telegram.dto.update.TelegramAudioUpdate;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import com.grammr.telegram.dto.update.TelegramUpdate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@RequiredArgsConstructor
@Service
public class TelegramUpdateFactory {

  private final TelegramClient telegramClient;

  public TelegramUpdate createTelegramUpdate(Update update) {
    if (update.getMessage().hasAudio()) {
      return createAudioUpdate(update);
    } else {
      return createTextUpdate(update);
    }
  }

  private TelegramTextUpdate createTextUpdate(Update update) {
    return TelegramTextUpdate.builder()
        .updateId(update.getUpdateId())
        .text(extractText(update))
        .chatId(extractChatId(update))
        .build();
  }

  @SneakyThrows
  private TelegramAudioUpdate createAudioUpdate(Update update) {
    var fileId = update.getMessage().getAudio().getFileId();
    var file = telegramClient.downloadFile(fileId);

    return TelegramAudioUpdate.builder()
        .filePath(file.toPath())
        .updateId(update.getUpdateId())
        .chatId(extractChatId(update))
        .build();
  }

  private String extractText(Update update) {
    var message = Optional.ofNullable(update.getMessage());
    return message.map(Message::getText).orElse("");
  }

  private long extractChatId(Update update) {
    if (update.hasCallbackQuery()) {
      return update.getCallbackQuery().getMessage().getChatId();
    } else {
      return update.getMessage().getChatId();
    }
  }
}
