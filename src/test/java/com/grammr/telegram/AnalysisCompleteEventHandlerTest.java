package com.grammr.telegram;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.common.FullAnalysisStringificationService;
import com.grammr.domain.entity.Request;
import com.grammr.domain.entity.Request.Status;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.domain.value.FullAnalysis;
import com.grammr.domain.value.RequestStats;
import com.grammr.service.RequestService;
import com.grammr.service.UserService;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.response.TelegramTextResponse;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.BlockingQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnalysisCompleteEventHandlerTest {

  @Mock
  private BlockingQueue<TelegramResponse> outgoingMessageQueue;

  @Mock
  private RequestService requestService;

  @Mock
  private UserService userService;

  @Mock
  private FullAnalysisStringificationService ignored;

  @InjectMocks
  private AnalysisCompleteEventHandler analysisCompleteEventHandler;

  @Test
  void shouldSendDebugInformationIfEnabled() {
    var user = UserSpec.valid().debug(true).build();
    var request = Request.builder()
        .chatId(1L)
        .status(Status.COMPLETED)
        .requestId("requestId")
        .createdTimestamp(ZonedDateTime.now())
        .updatedTimestamp(ZonedDateTime.now().plus(1500, ChronoUnit.MILLIS))
        .completionTokens(500)
        .promptTokens(100)
        .build();

    var analysisCompleteEvent = AnalysisCompleteEvent.builder()
        .fullAnalysis(FullAnalysis.builder().build())
        .requestId("requestId").build();

    when(userService.findUserByChatId(1L)).thenReturn(user);
    when(requestService.findRequest("requestId")).thenReturn(request);
    when(requestService.complete(analysisCompleteEvent, request)).thenReturn(request);

    analysisCompleteEventHandler.handleAnalysisCompleteEvent(analysisCompleteEvent);

    var captor = ArgumentCaptor.forClass(TelegramTextResponse.class);
    verify(outgoingMessageQueue, times(2)).add(captor.capture());

    var responses = captor.getAllValues();
    var debugResponse = responses.get(1);
    assertThat(debugResponse.getText()).isEqualTo(RequestStats.builder()
        .requestTime(1500)
        .completionTokens(500)
        .promptTokens(100).build().toString());
  }
}