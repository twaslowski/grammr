package com.grammr.telegram;

import com.grammr.common.FullAnalysisStringificationService;
import com.grammr.domain.entity.Request;
import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.domain.value.RequestStats;
import com.grammr.service.RequestService;
import com.grammr.service.UserService;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.response.TelegramTextResponse;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisCompleteEventHandler {

  private final BlockingQueue<TelegramResponse> outgoingMessageQueue;
  private final RequestService requestService;
  private final UserService userService;
  private final FullAnalysisStringificationService fullAnalysisStringificationService;

  @Async
  @EventListener
  protected void handleAnalysisCompleteEvent(AnalysisCompleteEvent analysisCompleteEvent) {
    var request = requestService.findRequest(analysisCompleteEvent.requestId());
    request = requestService.complete(analysisCompleteEvent, request);

    var telegramResponse = telegramResponseFrom(analysisCompleteEvent, request.getChatId());
    outgoingMessageQueue.add(telegramResponse);

    createDebugResponse(request).ifPresent(outgoingMessageQueue::add);
  }

  private TelegramTextResponse telegramResponseFrom(AnalysisCompleteEvent event, long chatId) {
    return TelegramTextResponse.builder()
        .chatId(chatId)
        .text(fullAnalysisStringificationService.stringifyAnalysis(event.fullAnalysis()))
        .build();
  }

  private Optional<TelegramResponse> createDebugResponse(Request request) {
    var user = userService.findUserByChatId(request.getChatId());
    if (user.debug()) {
      var requestStats = RequestStats.from(request);
      return Optional.of(TelegramTextResponse.builder()
          .chatId(request.getChatId())
          .text(requestStats.toString())
          .build());
    }
    return Optional.empty();
  }
}
