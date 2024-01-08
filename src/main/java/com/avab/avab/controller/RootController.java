package com.avab.avab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {
    @GetMapping("/health")
    public String healthCheck() {
        return "I'm Healthy!";
    }
}
