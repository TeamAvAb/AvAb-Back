package com.avab.avab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/")
@Tag(name = "Root 🌐", description = "루트 API")
public class RootController {

    @GetMapping("/health")
    @Operation(summary = "Health Check API", description = "배포 서버 헬스 체크 API")
    public String healthCheck() {
        return "I'm Healthy!";
    }
}
