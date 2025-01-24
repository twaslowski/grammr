package com.grammr.telegram.service;

import com.grammr.domain.entity.Request;
import com.grammr.domain.entity.User;
import com.grammr.domain.event.AnalysisRequest;
import com.grammr.repository.RequestRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisInitiationService {

  private final RequestRepository requestRepository;
  private final ApplicationEventPublisher eventPublisher;

  public void initiateAnalysis(String phrase, User user) {
    var requestId = UUID.randomUUID().toString();
    var analysisRequestEvent = AnalysisRequest.full()
        .phrase(phrase)
        .userLanguageLearned(user.getLanguageLearned())
        .userLanguageSpoken(user.getLanguageSpoken())
        .requestId(requestId)
        .build();

    var request = Request.from(requestId, user.getChatId());
    requestRepository.save(request);
    log.info("Saved request with id {}", requestId);
    eventPublisher.publishEvent(analysisRequestEvent);
  }
}
