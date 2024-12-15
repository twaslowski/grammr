package com.grammr.telegram;

import com.grammr.common.MessageUtil;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.response.TelegramTextResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramErrorHandler {

  private final MessageUtil messageUtil;

  public TelegramResponse noHandlerFound(long chatId) {
    return TelegramTextResponse.builder()
        .text(messageUtil.getMessage("telegram.error.handler.not-found"))
        .chatId(chatId)
        .build();
  }

  public TelegramResponse error(long chatId) {
    return TelegramTextResponse.builder()
        .text(messageUtil.getMessage("telegram.error.generic"))
        .chatId(chatId)
        .build();
  }

  public TelegramResponse userUnknown(long chatId) {
    return TelegramTextResponse.builder()
        .text(messageUtil.getMessage("telegram.error.user.not-found"))
        .chatId(chatId)
        .build();
  }

}
