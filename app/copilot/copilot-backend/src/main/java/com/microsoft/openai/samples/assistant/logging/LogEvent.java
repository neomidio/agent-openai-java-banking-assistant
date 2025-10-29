package com.microsoft.openai.samples.assistant.logging;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LogEvent(
        long timestamp,
        String level,
        String logger,
        String thread,
        String message,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String stackTrace,
        Map<String, String> mdc
) {
}
