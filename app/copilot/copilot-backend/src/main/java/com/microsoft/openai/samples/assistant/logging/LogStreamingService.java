package com.microsoft.openai.samples.assistant.logging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Service
public class LogStreamingService {

    private static final long DEFAULT_TIMEOUT = TimeUnit.MINUTES.toMillis(30);

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Deque<LogEvent> recentEvents = new ArrayDeque<>();
    private final int historySize;

    public LogStreamingService(@Value("${assistant.logging.stream.history-size:200}") int historySize) {
        this.historySize = Math.max(historySize, 0);
    }

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            emitter.complete();
        });
        emitter.onError(throwable -> emitters.remove(emitter));

        List<LogEvent> snapshot;
        synchronized (recentEvents) {
            snapshot = new ArrayList<>(recentEvents);
        }
        snapshot.forEach(event -> safeSend(emitter, event));

        return emitter;
    }

    public void publish(LogEvent logEvent) {
        if (logEvent == null) {
            return;
        }

        synchronized (recentEvents) {
            if (historySize > 0 && recentEvents.size() >= historySize) {
                recentEvents.removeFirst();
            }
            if (historySize > 0) {
                recentEvents.addLast(logEvent);
            }
        }

        emitters.forEach(emitter -> safeSend(emitter, logEvent));
    }

    public List<LogEvent> recentEvents() {
        synchronized (recentEvents) {
            return new ArrayList<>(recentEvents);
        }
    }

    private void safeSend(SseEmitter emitter, LogEvent event) {
        try {
            emitter.send(SseEmitter.event().name("log").data(event));
        } catch (IOException exception) {
            emitters.remove(emitter);
            emitter.completeWithError(exception);
        }
    }
}
