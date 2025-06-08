package com.grammr.chat.controller.v2.dto;

import com.grammr.chat.value.Message;

public record ChatInitializedDto(
    ChatDto chat,
    Message response
) {

}
