package com.grammr.telegram.handler.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.common.MessageUtil;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.service.UserService;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SetLanguageHandlerTest {

  @Mock
  private MessageUtil messageUtil;

  @Mock
  private UserService userService;

  @InjectMocks
  private SetLanguageHandler setLanguageHandler;

  @Test
  void happyPath() {
    var validUpdate = TelegramTextUpdate.builder()
        .text("/setlanguage learning DE")
        .chatId(1)
        .build();

    var user = UserSpec.valid()
        .languageLearned(LanguageCode.EN)
        .build();
    when(userService.findUserByChatId(1)).thenReturn(user);

    setLanguageHandler.handleUpdate(validUpdate);

    assertThat(user.getLanguageLearned()).isEqualTo(LanguageCode.DE);
    verify(messageUtil).parameterizeMessage("command.setlanguage.success", "learning", "German");
  }

  @Test
  void shouldThrowExceptionIfLanguageCodeIsInvalid() {
    var validUpdate = TelegramTextUpdate.builder()
        .text("/setlanguage learning ES")
        .chatId(1)
        .build();

    setLanguageHandler.handleUpdate(validUpdate);

    verify(messageUtil).parameterizeMessage("command.setlanguage.error",
        "Language ES is not available yet");
  }

  @Test
  void shouldThrowExceptionIfTypeIsInvalid() {
    var validUpdate = TelegramTextUpdate.builder()
        .text("/setlanguage invalid DE")
        .chatId(1)
        .build();

    setLanguageHandler.handleUpdate(validUpdate);

    verify(messageUtil).parameterizeMessage("command.setlanguage.error",
        "The first argument should be 'LEARNING' or 'SPEAKING'");
  }
}