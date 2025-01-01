package com.controlleradvicelogging.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HttpRequestLogger {

    private static final String LOG_NAME = "http-request-log";

    private final HttpLoggerUtil httpLoggerUtil;

    public HttpRequestLogger(HttpLoggerUtil httpLoggerUtil) {
        this.httpLoggerUtil = httpLoggerUtil;
    }

    public void log(HttpServletRequest request) {
        if (httpLoggerUtil.shouldLog()) {
            logEntries(httpLoggerUtil.buildRequestLogEntries(request));
        }
    }

    public void logWithBody(HttpServletRequest request, Object requestBody) {
        if (httpLoggerUtil.shouldLog()) {
            logEntries(httpLoggerUtil.buildRequestLogEntriesWithBody(request, requestBody));
        }
    }

    private void logEntries(Map<String, Object> logEntries) {
        List<String> logs = new ArrayList<>();
        logEntries.forEach((key, value) -> logs.add(key + ": " + value));
        log.info(LOG_NAME + " " + logs);
    }
}
