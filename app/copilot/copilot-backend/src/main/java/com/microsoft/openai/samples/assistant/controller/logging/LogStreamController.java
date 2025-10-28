package com.microsoft.openai.samples.assistant.controller.logging;

import com.microsoft.openai.samples.assistant.logging.LogEvent;
import com.microsoft.openai.samples.assistant.logging.LogStreamingService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogStreamController {

    private final LogStreamingService logStreamingService;

    public LogStreamController(LogStreamingService logStreamingService) {
        this.logStreamingService = logStreamingService;
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLogs() {
        return logStreamingService.createEmitter();
    }

    @GetMapping
    public List<LogEvent> recentLogs() {
        return logStreamingService.recentEvents();
    }
}
