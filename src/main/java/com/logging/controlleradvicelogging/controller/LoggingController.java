package com.logging.controlleradvicelogging.controller;

import com.logging.controlleradvicelogging.model.FooBar;
import com.logging.controlleradvicelogging.service.LoggingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logging")
public class LoggingController {

    private final LoggingService loggingService;

    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @GetMapping()
    public ResponseEntity<String> getFooBar() {
        return ResponseEntity.ok(loggingService.getFooBar());
    }

    @PostMapping("/foo-bar")
    public ResponseEntity<FooBar> testBody(@RequestBody FooBar fooBar) {
        return ResponseEntity.ok(loggingService.testBody(fooBar));
    }

    @PostMapping("/{testPathVariable}/test")
    public ResponseEntity<String> testPath(@PathVariable String testPathVariable,
                                           @RequestParam("testRequestParam") String testRequestParam) {
        return ResponseEntity.ok(loggingService.testPath(testPathVariable, testRequestParam));
    }

    @GetMapping("/test-health")
    public ResponseEntity<String> testExcludedFromLogging() {
        return ResponseEntity.ok(loggingService.testExcludedFromLogging());
    }
}
