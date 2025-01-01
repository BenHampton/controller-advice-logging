package com.controlleradvicelogging.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class HttpResponseLogger {

    private static final String LOG_NAME = "http-response-log";

    private final HttpLoggerUtil httpLoggerUtil;

    public HttpResponseLogger(HttpLoggerUtil httpLoggerUtil) {
        this.httpLoggerUtil = httpLoggerUtil;
    }

    public void log(HttpServletRequest request, HttpServletResponse response, Object responseBody) {
        if (httpLoggerUtil.shouldLogResponse(request)) {
            Map<String, Object> logEntries = httpLoggerUtil.buildResponseLogEntries(response, responseBody);
            List<String> logs = new ArrayList<>();
            logEntries.forEach((key, value) -> logs.add(key + ": " + value));
            log.info(LOG_NAME + " " + logs);
        }
    }
}
