package com.mental.cove.service;

import com.mental.cove.data.Questionnaire;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QuestionnaireService {

    public Questionnaire loadMBTIQuestions() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new ClassPathResource("questionnaire/mbti_questions.json").getInputStream(), Questionnaire.class);
    }
}
