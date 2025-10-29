package com.microsoft.openai.samples.assistant.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import org.springframework.stereotype.Component;

@Component
public class LogStreamingAppender extends AppenderBase<ILoggingEvent> {

    private final LogStreamingService logStreamingService;

    public LogStreamingAppender(LogStreamingService logStreamingService) {
        this.logStreamingService = logStreamingService;
        setName("LOG_STREAMING_APPENDER");
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (logStreamingService == null || eventObject == null) {
            return;
        }

        LogEvent logEvent = new LogEvent(
                eventObject.getTimeStamp(),
                level(eventObject),
                eventObject.getLoggerName(),
                eventObject.getThreadName(),
                eventObject.getFormattedMessage(),
                extractStackTrace(eventObject),
                eventObject.getMDCPropertyMap()
        );

        logStreamingService.publish(logEvent);
    }

    private String level(ILoggingEvent eventObject) {
        Level level = eventObject.getLevel();
        return level != null ? level.toString() : null;
    }

    private String extractStackTrace(ILoggingEvent eventObject) {
        IThrowableProxy throwableProxy = eventObject.getThrowableProxy();
        if (throwableProxy == null) {
            return null;
        }
        return ThrowableProxyUtil.asString(throwableProxy);
    }
}
