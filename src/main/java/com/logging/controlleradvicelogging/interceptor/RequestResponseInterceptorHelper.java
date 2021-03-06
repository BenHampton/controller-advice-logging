package com.logging.controlleradvicelogging.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Component
public class RequestResponseInterceptorHelper {

    public static final String HEADER_VALUE_HIDDEN_VALUE = "**********";

    private boolean logRequestHeaders;

    private boolean logRequestBody;

    private boolean logRequestUri;

    private boolean logRequestMethod;

    private boolean logRequestParameters;

    private boolean logResponseHeaders;

    private boolean logResponseBody;

    public RequestResponseInterceptorHelper(@Value("${logging.controller.request.method:true}") boolean logRequestMethod,
                                            @Value("${logging.controller.request.uri:true}") boolean logRequestUri,
                                            @Value("${logging.controller.request.parameters:true}") boolean logRequestParameters,
                                            @Value("${logging.controller.request.headers:false}") boolean logRequestHeaders,
                                            @Value("${logging.controller.request.body:true}") boolean logRequestBody,
                                            @Value("${logging.controller.response.headers:false}") boolean logResponseHeaders,
                                            @Value("${logging.controller.response.body:true}") boolean logResponseBody) {
        this.logRequestMethod = logRequestMethod;
        this.logRequestUri = logRequestUri;
        this.logRequestParameters = logRequestParameters;
        this.logRequestHeaders = logRequestHeaders;
        this.logRequestBody = logRequestBody;
        this.logResponseHeaders = logResponseHeaders;
        this.logResponseBody = logResponseBody;
    }

    public String buildFromRequest(HttpServletRequest httpServletRequest, Object body) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestBodyMap = mapper.convertValue(body, Map.class);

        StringBuilder stringBuilder = new StringBuilder("REQUEST ");
        if(logRequestMethod) {
            stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
        }
        if(logRequestUri) {
            stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
        }
        if (logRequestParameters) {
            stringBuilder.append("parameters=[").append(buildParametersMap(httpServletRequest)).append("] ");
        }
        if(logRequestHeaders) {
            stringBuilder.append("headers=[").append(buildRequestHeadersMap(httpServletRequest)).append("] ");
        }
        if (logRequestBody && body != null) {
            stringBuilder.append("body=[").append(requestBodyMap).append("]");
        }
        return stringBuilder.toString();
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        httpServletRequest.getParameterMap().forEach((key, value) -> resultMap.put(key, Arrays.toString(value)));
        return resultMap;
    }

    private Map<String, String> buildRequestHeadersMap(HttpServletRequest request) {
        List<String> headersToExclude = Arrays.asList(
                HttpHeaders.COOKIE.toLowerCase(),
                HttpHeaders.AUTHORIZATION.toLowerCase());

        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            if (!headersToExclude.contains(key.toLowerCase())){
                map.put(key, value);
            } else {
                map.put(key, HEADER_VALUE_HIDDEN_VALUE);
            }
        }
        return map;
    }

    public void logResponse(HttpServletRequest request, HttpServletResponse response, Object responseBody) {
        String uri = request.getRequestURI().toLowerCase();
        if (!uri.contains("/actuator")) {
            log.info("Response: " + responseBody);
        }
    }

    public String buildFromResponse(HttpServletResponse httpServletResponse, Object body) {
        StringBuilder stringBuilder = new StringBuilder("RESPONSE ");
        stringBuilder.append("status=[").append(httpServletResponse.getStatus()).append("] ");
        if(logResponseHeaders) {
            stringBuilder.append("headers=[").append(buildResponseHeadersMap(httpServletResponse)).append("] ");
        }
        if(logResponseBody) {
            stringBuilder.append("body=[").append(body).append("] ");
        }
        return stringBuilder.toString();
    }

    private Map<String, String> buildResponseHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }
        return map;
    }

}