package com.dev.indianadapi.authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    @GetMapping("/info/")
    public ResponseEntity<String> infoHandler() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping("/secured/")
    public ResponseEntity<String> securedHandler() {
        return ResponseEntity.ok("secured");
    }
}
