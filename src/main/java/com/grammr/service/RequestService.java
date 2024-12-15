package com.grammr.service;

import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.domain.exception.RequestNotFoundException;
import com.grammr.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestService {

  private final RequestRepository requestRepository;

  public long retrieveRequestChatId(String requestId) {
    return requestRepository.findByRequestId(requestId)
        .orElseThrow(() -> new RequestNotFoundException(requestId))
        .getChatId();
  }

  @Async
  @EventListener
  @Transactional
  public void update(AnalysisCompleteEvent event) {
    requestRepository.findByRequestId(event.requestId())
        .orElseThrow(() -> new RequestNotFoundException(event.requestId()))
        .complete(event);
  }
}
