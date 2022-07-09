package com.logging.controlleradvicelogging.service;

import com.logging.controlleradvicelogging.model.FooBar;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    public String getFooBar() {
        return "FOO BAR";
    }

    public String testPath(String testPathVariable, String testRequestParam) {
        return "testPathVariable: " + testPathVariable + " testRequestParam: " + testRequestParam;
    }

    public FooBar testBody(FooBar fooBar) {
        return fooBar;
    }

    public String testExcludedFromLogging() {
        return "Should Not See Request/Response Logs";
    }
}
