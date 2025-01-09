package com.mental.cove.controller;

import com.mental.cove.data.MBTIQuestionnaire;
import com.mental.cove.service.QuestionnaireService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/questionnaires")
public class QuestionnaireController {
    @Resource
    private QuestionnaireService questionnaireService;

    @GetMapping("/mbti")
    public MBTIQuestionnaire getMBTIQuestionnaire() throws IOException {
        return questionnaireService.loadMBTIQuestions();
    }

    @PostMapping("/submit-mbti")
    public ResponseEntity<Object> submitMBTI(@RequestBody Map<String, String> mbtiAnswers) throws IOException {
        return ResponseEntity.ok(questionnaireService.calculateMBTIResult(mbtiAnswers));
    }

    @GetMapping("/assessment-results")
    public ResponseEntity<Object> getAssessmentResults(@RequestParam String userId) {
        return ResponseEntity.ok(questionnaireService.getAssessmentResults(Long.valueOf(userId)));
    }
}
