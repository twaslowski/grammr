package com.grammr.telegram.handler.command;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.common.MessageUtil;
import com.grammr.service.UserService;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DebugHandlerTest {

  @Mock
  private UserService userService;

  @Mock
  private MessageUtil messageUtil;

  @InjectMocks
  private DebugHandler debugHandler;

  @Test
  void shouldEnableDebug() {
    when(userService.toggleDebug(1L)).thenReturn(true);

    var update = TelegramTextUpdate.builder()
        .text("/debug").chatId(1L).build();

    debugHandler.handleUpdate(update);

    verify(messageUtil).parameterizeMessage("command.debug.message", true);
  }
}