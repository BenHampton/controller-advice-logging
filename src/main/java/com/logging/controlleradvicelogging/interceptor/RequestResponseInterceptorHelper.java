package com.logging.controlleradvicelogging.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RequestResponseInterceptorHelper {

    public String getRequestLogMessage(HttpServletRequest request, Object requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestBodyMap = mapper.convertValue(requestBody, Map.class);
        String headers = getHeaders(request);
        return "Request: [Method: " + request.getMethod() + "]" +
                " [Path: " + request.getRequestURI() + "]" +
                " [Headers: " + headers + "]" +
                " [ReqNo: " + requestBodyMap.get("reqNo") + "]";
    }

    public void logResponse(HttpServletRequest request, HttpServletResponse response, Object responseBody) {
        String uri = request.getRequestURI().toLowerCase();
        if (!uri.contains("/actuator")) {
            log.info("Response: " + responseBody);
        }
    }

    private String getHeaders(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headersMap.put(key, value);
        }
        headersMap.computeIfPresent("authorization", (k, v) -> "null".equals(v) ? "null" : "*****");
        return headersMap.toString();
    }
}