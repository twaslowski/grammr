package com.grammr.telegram.external.factory;

import static java.lang.String.format;

import com.grammr.telegram.dto.update.TelegramAudioUpdate;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import com.grammr.telegram.dto.update.TelegramUpdate;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@RequiredArgsConstructor
@Service
public class TelegramUpdateFactory {

  private final TelegramClient telegramClient;

  public TelegramUpdate createTelegramUpdate(Update update) {
    if (update.getMessage().hasVoice()) {
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
    var filePath = downloadFile(update);
    return TelegramAudioUpdate.builder()
        .filePath(filePath)
        .updateId(update.getUpdateId())
        .chatId(extractChatId(update))
        .build();
  }

  @SneakyThrows
  private Path downloadFile(Update update) {
    var filename = format("%s_%s.ogg", extractChatId(update), UUID.randomUUID());

    var fileId = update.getMessage().getVoice().getFileId();
    var getFile = telegramClient.execute(GetFile.builder().fileId(fileId).build());
    var filePath = telegramClient.downloadFile(getFile.getFilePath()).toPath();

    filePath = renameFileWithinDirectory(filePath, filename);
    return filePath;
  }

  @SneakyThrows
  private Path renameFileWithinDirectory(Path file, String newName) {
    var directory = file.getParent();
    var newFile = directory.resolve(newName);
    Files.move(file, newFile);
    return newFile;
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
