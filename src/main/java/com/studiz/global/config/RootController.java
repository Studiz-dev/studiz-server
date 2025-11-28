package com.studiz.global.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 루트 경로 컨트롤러
 * context-path가 /api로 설정되어 있으므로 @RequestMapping은 필요 없음
 */
@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> root() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Studiz Server API");
        response.put("status", "running");
        response.put("swagger", "/api/swagger-ui.html");
        return ResponseEntity.ok(response);
    }
}

