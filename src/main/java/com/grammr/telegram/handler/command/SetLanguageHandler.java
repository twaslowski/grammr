package com.grammr.telegram.handler.command;

import com.grammr.common.MessageUtil;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.exception.LanguageNotAvailableException;
import com.grammr.service.UserService;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.response.TelegramTextResponse;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import com.grammr.telegram.dto.update.TelegramUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SetLanguageHandler extends AbstractCommandHandler {

  private static final String COMMAND = "/setlanguage";

  private enum Type {
    LEARNING, SPEAKING
  }

  private final UserService userService;
  private final MessageUtil messageUtil;

  @Override
  String getCommand() {
    return COMMAND;
  }

  @Override
  @Transactional
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    try {
      // parse message
      var components = update.getText().split(" ");
      var type = parseType(components[1]);
      var languageCode = parseLanguageCode(components[2]);

      // set user language preferences
      var user = userService.findUserByChatId(update.getChatId());
      switch (type) {
        case LEARNING -> user.setLanguageLearned(languageCode);
        case SPEAKING -> user.setLanguageSpoken(languageCode);
        default -> throw new IllegalArgumentException("The first argument should be 'LEARNING' or 'SPEAKING'");
      }

      return TelegramTextResponse.builder()
          .text(successMessage(type, languageCode))
          .chatId(update.getChatId())
          .build();

      // error handling
    } catch (Exception e) {
      return TelegramTextResponse.builder()
          .text(messageUtil.parameterizeMessage("command.setlanguage.error", e.getMessage()))
          .chatId(update.getChatId())
          .build();
    }
  }

  private String successMessage(Type type, LanguageCode languageCode) {
    return messageUtil.parameterizeMessage("command.setlanguage.success",
        type.toString().toLowerCase(), languageCode.getLanguageName());
  }

  private Type parseType(String type) {
    try {
      return Type.valueOf(type.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("The first argument should be 'LEARNING' or 'SPEAKING'");
    }
  }

  private LanguageCode parseLanguageCode(String languageCode) {
    try {
      return LanguageCode.valueOf(languageCode.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new LanguageNotAvailableException(languageCode);
    }
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update instanceof TelegramTextUpdate
        && update.getText().split(" ")[0].equals(getCommand());
  }
}
