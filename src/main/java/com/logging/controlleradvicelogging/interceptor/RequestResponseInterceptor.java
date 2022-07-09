package com.logging.controlleradvicelogging.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.sql.Array;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Slf4j
@Component
public class RequestResponseInterceptor extends RequestBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    private final String ACTUATOR_HEALTH_ENDPOINT = "/health";

    private final HttpServletRequest request;

    private final RequestResponseInterceptorHelper requestResponseInterceptorHelper;

    private final String[] uriEndsWithExclusions;

    private final boolean requestLoggingEnabled;

    private final boolean responseLoggingEnabled;

    public RequestResponseInterceptor(HttpServletRequest request,
                                      RequestResponseInterceptorHelper requestResponseInterceptorHelper,
                                      @Value("${logging.controller.exclusions.uri-ends-with:}") String[] uriEndsWithExclusions,
                                      @Value("${logging.controller.request.enable:true}") boolean requestLoggingEnabled,
                                      @Value("${logging.controller.response.enable:true}") boolean responseLoggingEnabled) {
        this.request = request;
        this.requestResponseInterceptorHelper = requestResponseInterceptorHelper;
        this.uriEndsWithExclusions = uriEndsWithExclusions;
        this.requestLoggingEnabled = requestLoggingEnabled;
        this.responseLoggingEnabled = responseLoggingEnabled;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (requestLoggingEnabled && !excludeUriFromLogging(request.getRequestURI())) {
            log.info(requestResponseInterceptorHelper.buildFromRequest(request, o));
        }
        return super.afterBodyRead(o, inputMessage, parameter, targetType, converterType);
    }

    private boolean excludeUriFromLogging(String uri) {
        boolean test = Arrays.stream(uriEndsWithExclusions).anyMatch(exclusion -> uri.toLowerCase().endsWith(exclusion.toLowerCase()));
        return uri.toLowerCase().endsWith(ACTUATOR_HEALTH_ENDPOINT) ||
                Arrays.stream(uriEndsWithExclusions).anyMatch(exclusion -> uri.toLowerCase().endsWith(exclusion.toLowerCase()));
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        if (responseLoggingEnabled && !excludeUriFromLogging(request.getRequestURI())) {
            log.info(requestResponseInterceptorHelper.buildFromResponse((
                    (ServletServerHttpResponse) serverHttpResponse).getServletResponse(), o));
        }
        return o;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }


}