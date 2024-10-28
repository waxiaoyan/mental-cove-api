package com.mental.cove.data;

import lombok.Data;

import java.util.List;

@Data
public class Questionnaire {
    private List<Question> questions;
}
