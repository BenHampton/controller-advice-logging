package com.controlleradvicelogging.logging;

import com.controlleradvicelogging.config.RequestProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class HttpLoggerUtil {

    private final RequestProperties requestProperties;

    private final ObjectMapper objectMapper;

    public HttpLoggerUtil(RequestProperties requestProperties, ObjectMapper objectMapper) {
        this.requestProperties = requestProperties;
        this.objectMapper = objectMapper;
    }

    public boolean shouldLogResponse(HttpServletRequest request) {
        return shouldLog() && !containsActuator(request.getRequestURI());
    }

    public boolean shouldLog() {
        return requestProperties.isCanLog();
    }

    private boolean containsActuator(String uri) {
        return uri.toLowerCase().contains("/actuator");
    }

    public Map<String, Object> buildResponseLogEntries(HttpServletResponse response, Object responseBody) {
        Map<String, Object> logEntries = new HashMap<>();
        logEntries.put("body", responseBody);
        logEntries.put("responseCode", response.getStatus());
        return logEntries;
    }

    public Map<String, Object> buildRequestLogEntriesWithBody(HttpServletRequest request, Object requestBody) {
        Map<String, Object> logEntries = buildRequestLogEntries(request);
        logEntries.put("body", requestBody);
        return logEntries;
    }

    public Map<String, Object> buildRequestLogEntries(HttpServletRequest request) {
        Map<String, Object> logEntries = new HashMap<>();
        logEntries.put("method", request.getMethod());
        logEntries.put("path", request.getRequestURI() + getQueryParameters(request));
        try {
            logEntries.put("headers", objectMapper.writeValueAsString(getHeaders(request)));
        } catch (JsonProcessingException e) {
            logEntries.put("headers", "error converting headers to json");
        }
        return logEntries;
    }

    private String getQueryParameters(HttpServletRequest request) {
        return request.getQueryString() == null ? "" : "?" + request.getQueryString();
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            headersMap.put(key, request.getHeader(key));
        }
        headersMap.computeIfPresent("authorization", (k, v) -> "null".equals(v) ? "null" : "*****");
        return headersMap;
    }
}
