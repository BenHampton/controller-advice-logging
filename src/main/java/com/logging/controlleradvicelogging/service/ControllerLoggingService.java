package com.logging.controlleradvicelogging.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

//@Component
//public class ControllerLoggingService {
//
//    public ControllerLoggingService(ControllerLogBuilder controllerLogBuilder,
//                                    @Value("${logging.controller.exclusions.uri-ends-with:}") String[] uriEndsWithExclusions,
//                                    @Value("${logging.controller.request.enable:true}") boolean requestLoggingEnabled,
//                                    @Value("${logging.controller.response.enable:true}") boolean responseLoggingEnabled) {
//        this.controllerLogBuilder = controllerLogBuilder;
//        this.uriEndsWithExclusions = uriEndsWithExclusions;
//        this.requestLoggingEnabled = requestLoggingEnabled;
//        this.responseLoggingEnabled = responseLoggingEnabled;
//    }
//
//    @Override
//    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
//        if (requestLoggingEnabled && !excludeUriFromLogging(httpServletRequest.getRequestURI())) {
//            log.info(controllerLogBuilder.buildFromRequest(httpServletRequest, body));
//        }
//    }
//
//    private boolean excludeUriFromLogging(String uri) {
//        return uri.toLowerCase().endsWith(ACTUATOR_HEALTH_ENDPOINT) ||
//                Arrays.stream(uriEndsWithExclusions).anyMatch(exclusion -> uri.toLowerCase().endsWith(exclusion.toLowerCase()));
//    }
//
//    @Override
//    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
//        if (responseLoggingEnabled && !excludeUriFromLogging(httpServletRequest.getRequestURI())) {
//            log.info(controllerLogBuilder.buildFromResponse(httpServletResponse, body));
//        }
//    }
//
//}