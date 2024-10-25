package com.mental.cove.controller;

import com.mental.cove.annotation.RateLimit;
import com.mental.cove.service.GeminiService;
import com.mental.cove.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/openai")
public class OpenAIController {
    @Autowired
    private OpenAIService openAIService;

    @PostMapping(value = "/chat")
    @RateLimit(requests = 50, duration = 60)
    public ResponseEntity<?> chat(@RequestBody String prompt) {
        return ResponseEntity.ok(openAIService.chat(prompt));
    }
}
