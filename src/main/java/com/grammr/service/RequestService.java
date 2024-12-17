package com.grammr.service;

import com.grammr.domain.entity.Request;
import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.domain.exception.RequestNotFoundException;
import com.grammr.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

  public Request findRequest(String requestId) {
    return requestRepository.findByRequestId(requestId)
        .orElseThrow(() -> new RequestNotFoundException(requestId));
  }

  public Request complete(AnalysisCompleteEvent event, Request request) {
    request.complete(event);
    return requestRepository.save(request);
  }
}
