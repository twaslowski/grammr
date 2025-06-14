package com.grammr.chat.controller.dto;

import com.grammr.chat.value.Message;

public record ChatInitializedDto(
    ChatDto chat,
    Message response
) {

}
