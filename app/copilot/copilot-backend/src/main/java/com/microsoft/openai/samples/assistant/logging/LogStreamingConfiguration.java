package com.microsoft.openai.samples.assistant.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class LogStreamingConfiguration {

    private final LogStreamingAppender logStreamingAppender;

    public LogStreamingConfiguration(LogStreamingAppender logStreamingAppender) {
        this.logStreamingAppender = logStreamingAppender;
    }

    @PostConstruct
    public void attachAppender() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);

        if (logStreamingAppender.getContext() == null) {
            logStreamingAppender.setContext(context);
        }

        if (!logStreamingAppender.isStarted()) {
            logStreamingAppender.start();
        }

        if (rootLogger.getAppender(logStreamingAppender.getName()) == null) {
            rootLogger.addAppender(logStreamingAppender);
        }
    }
}
