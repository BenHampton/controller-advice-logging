package com.logging.controlleradvicelogging.controller;

import com.logging.controlleradvicelogging.service.FooService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foo")
public class FooController {

    private FooService fooService;

    public FooController(FooService fooService) {
        this.fooService = fooService;
    }

    @GetMapping
    public ResponseEntity<String> getFoo() {
        return ResponseEntity.ok(fooService.getFoo());
    }
}
