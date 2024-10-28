package com.grammr.port.telegram.dto.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class TelegramTextResponse extends TelegramResponse {

  static ResponseType responseType = ResponseType.TEXT;
  String text;

  @Builder
  public TelegramTextResponse(long chatId, String text) {
    super(chatId);
    this.text = text;
  }

  @Override
  public ResponseType getResponseType() {
    return responseType;
  }
}
