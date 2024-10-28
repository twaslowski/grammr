package com.grammr.port.telegram.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.twaslowski.moodtracker.adapter.telegram.MessageUtil;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramInlineKeyboardResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.response.TelegramTextResponse;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramInlineKeyboardUpdate;
import de.twaslowski.moodtracker.adapter.telegram.dto.update.TelegramUpdate;
import de.twaslowski.moodtracker.adapter.telegram.handler.callback.CallbackGenerator;
import de.twaslowski.moodtracker.entity.metric.Metric;
import de.twaslowski.moodtracker.entity.metric.MetricDatapoint;
import de.twaslowski.moodtracker.service.RecordService;
import de.twaslowski.moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InlineKeyboardUpdateHandler implements UpdateHandler {

  private final ObjectMapper objectMapper;
  private final RecordService recordService;
  private final CallbackGenerator callbackGenerator;
  private final MessageUtil messageUtil;
  private final UserService userService;

  @Override
  @SneakyThrows
  @Transactional
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var inlineKeyboardUpdate = (TelegramInlineKeyboardUpdate) update;
    log.info("Received inline keyboard update with callback: {}", inlineKeyboardUpdate.getCallbackData());

    var user = userService.findByTelegramId(update.getChatId());
    var existingRecord = recordService.findIncompleteRecordsForUser(user.getId());
    return existingRecord
        .map(record -> enrichExistingRecord(record, inlineKeyboardUpdate))
        .orElseGet(() -> noRecordInProgressResponse(update));
  }

  @SneakyThrows
  private TelegramResponse enrichExistingRecord(Record record, TelegramInlineKeyboardUpdate update) {
    var receivedMetric = objectMapper.readValue(update.getCallbackData(), MetricDatapoint.class);
    record.updateMetric(receivedMetric);

    log.info("Updated record {} with metric {}, value {}",
        record.getId(), receivedMetric.metricId(), receivedMetric.value());
    return recordService.getNextIncompleteMetric(record)
        .map(nextMetric -> sendNextMetric(update, nextMetric))
        .orElseGet(() -> completeRecord(update));
  }

  private TelegramResponse completeRecord(TelegramUpdate update) {
    log.info("Completing record for user with chatId {}", update.getChatId());
    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .text(messageUtil.getMessage("command.record.saved"))
        .build();
  }

  private TelegramResponse sendNextMetric(TelegramUpdate update, Metric nextMetric) {
    log.info("Sending next metric [id={} name={}] for incomplete record", nextMetric.getId(), nextMetric.getName());
    return TelegramInlineKeyboardResponse.builder()
        .chatId(update.getChatId())
        .content(callbackGenerator.createCallbacks(nextMetric))
        .text(nextMetric.getDescription())
        .build();
  }

  private TelegramResponse noRecordInProgressResponse(TelegramUpdate update) {
    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .text(messageUtil.getMessage("command.record.not-recording"))
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    // todo this should be a more specific check in the future
    return update.hasCallback();
  }
}
