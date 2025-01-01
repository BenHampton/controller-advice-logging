package com.controlleradvicelogging.interceptors;

import com.controlleradvicelogging.logging.HttpRequestLogger;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class BodilessRequestInterceptor implements HandlerInterceptor {

    private final HttpRequestLogger httpRequestLogger;

    public BodilessRequestInterceptor(HttpRequestLogger httpRequestLogger) {
        this.httpRequestLogger = httpRequestLogger;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name()) && requestIsBodiless(request)) {
            httpRequestLogger.log(request);
        }
        return true;
    }

    private boolean requestIsBodiless(HttpServletRequest request) {
        return HttpMethod.GET.name().equals(request.getMethod()) ||
                HttpMethod.DELETE.name().equals(request.getMethod());
    }
}
