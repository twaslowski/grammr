package com.grammr.domain.event;

import com.grammr.domain.entity.User;
import java.nio.file.Path;
import lombok.Builder;

@Builder
public record AudioTranscriptionRequestEvent(
    Path path,
    User user,
    String requestId
) {

}
