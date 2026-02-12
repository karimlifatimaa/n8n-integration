package com.example.n8nintegration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-error")
    public String triggerError() {
        throw new RuntimeException("Simulated error for n8n integration testing!");
    }
}
