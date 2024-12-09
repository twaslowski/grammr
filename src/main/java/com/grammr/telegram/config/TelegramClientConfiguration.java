package com.grammr.telegram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
@Profile("!test")
public class TelegramClientConfiguration {

  @Bean
  public TelegramClient telegramClient(@Value("${telegram.bot.token}") String token) {
    return new OkHttpTelegramClient(token);
  }
}
