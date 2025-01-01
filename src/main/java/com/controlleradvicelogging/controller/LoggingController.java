package com.controlleradvicelogging.controller;

import com.controlleradvicelogging.model.FooBar;
import com.controlleradvicelogging.service.LoggingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logging")
public class LoggingController {

    private final LoggingService loggingService;

    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @GetMapping()
    public ResponseEntity<String> findFooBar() {
        return ResponseEntity.ok(loggingService.getFooBar());
    }

    @PostMapping("/foo-bar")
    public ResponseEntity<FooBar> testBody(@RequestBody FooBar fooBar) {
        return ResponseEntity.ok(loggingService.testBody(fooBar));
    }

    @PostMapping("/{foo}/test")
    public ResponseEntity<String> testPath(@PathVariable String foo,
                                           @RequestParam("bar") String bar) {
        return ResponseEntity.ok(loggingService.testPath(foo, bar));
    }

    @GetMapping("/test/not-found")
    public ResponseEntity<String> testNotFoundException() {
        return ResponseEntity.ok(loggingService.testNotFoundException());
    }

    @GetMapping("/test/bad-request")
    public ResponseEntity<String> testBadRequestException() {
        return ResponseEntity.ok(loggingService.testBadRequestException());
    }

    @GetMapping("/test/forbidden")
    public ResponseEntity<String> testForbiddenException() {
        return ResponseEntity.ok(loggingService.testForbiddenException());
    }

    @GetMapping("/test/internal-server")
    public ResponseEntity<String> testInternalServerException() {
        return ResponseEntity.ok(loggingService.testInternalServerException());
    }

    @GetMapping("/test/conflict")
    public ResponseEntity<String> testConflictException() {
        return ResponseEntity.ok(loggingService.testConflictException());
    }
}
