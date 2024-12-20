package com.grammr.telegram.external;

import com.grammr.telegram.dto.update.TelegramUpdate;
import com.grammr.telegram.exception.UpdateNotProcessableException;
import com.grammr.telegram.external.factory.TelegramUpdateFactory;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!test")
public class TelegramUpdateProducer implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

  private final Queue<TelegramUpdate> incomingMessageQueue;
  private final TelegramUpdateFactory telegramUpdateFactory;

  @Value("${telegram.bot.token}")
  private String botToken;

  @Override
  public void consume(Update update) {
    try {
      var telegramUpdate = telegramUpdateFactory.createTelegramUpdate(update);
      log.info("Received update: {}, text: {}", telegramUpdate.getChatId(), telegramUpdate.getText());
      incomingMessageQueue.add(telegramUpdate);
    } catch (Exception e) {
      log.error("Failed to create TelegramUpdate from incoming update: {}", update, e);
      throw new UpdateNotProcessableException(e);
    }
  }

  // SpringLongPollingBot boilerplate
  @Override
  public String getBotToken() {
    return botToken;
  }

  @Override
  public LongPollingUpdateConsumer getUpdatesConsumer() {
    return this;
  }

  @AfterBotRegistration
  public void afterRegistration(BotSession botSession) {
    log.info("Successfully registered Telegram bot. Starting polling ...");
  }
}
