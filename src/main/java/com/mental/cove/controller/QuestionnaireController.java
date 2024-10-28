package com.mental.cove.controller;

import com.mental.cove.data.Questionnaire;
import com.mental.cove.service.QuestionnaireService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/questionnaires")
public class QuestionnaireController {
    @Resource
    private QuestionnaireService questionnaireService;

    @GetMapping("/mbti")
    public Questionnaire getMBTIQuestionnaire() throws IOException {
        return questionnaireService.loadMBTIQuestions();
    }
}
