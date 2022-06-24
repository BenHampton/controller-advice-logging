package com.logging.controlleradvicelogging.controller;

import com.logging.controlleradvicelogging.service.BarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/bar")
public class BarController {

    private final BarService barService;

    public BarController(BarService barService) {
        this.barService = barService;
    }

    @GetMapping()
    public ResponseEntity<String> getBar() {
        return ResponseEntity.ok(barService.getBar());
    }
}
