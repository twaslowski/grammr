package com.grammr.telegram.external;

import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaDownloadService {

  private final TelegramClient telegramClient;

  @SneakyThrows
  public Path downloadFile(String fileId) {
    log.info("Downloading file with id: {}", fileId);
    return telegramClient.downloadFile(fileId).toPath();
  }
}
