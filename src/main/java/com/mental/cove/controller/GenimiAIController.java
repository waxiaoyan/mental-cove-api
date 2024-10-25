package com.mental.cove.controller;

import com.mental.cove.annotation.RateLimit;
import com.mental.cove.service.GeminiService;
import com.mental.cove.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/genimi")
public class GenimiAIController {
    @Autowired
    private GeminiService geminiService;
    @PostMapping("/chat")
    public String geminiChat(@RequestBody String prompt) {
        return this.geminiService.chat(prompt);
    }
}
