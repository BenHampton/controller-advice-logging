package com.controlleradvicelogging.controller.advice;

import com.controlleradvicelogging.logging.HttpRequestLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

@Slf4j
@RestControllerAdvice
@Component
public class RequestControllerAdvice extends RequestBodyAdviceAdapter {

    private final HttpRequestLogger httpRequestLogger;

    private final HttpServletRequest request;

    public RequestControllerAdvice(HttpServletRequest request,
                                   HttpRequestLogger httpRequestLogger) {
        this.request = request;
        this.httpRequestLogger = httpRequestLogger;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        httpRequestLogger.logWithBody(request, o);
        return super.afterBodyRead(o, inputMessage, parameter, targetType, converterType);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
}
