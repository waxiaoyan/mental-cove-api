package com.mental.cove.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssessmentResultData {
    private String userId;
    private String assessmentType;
    private String assessmentResult;
    private String assessmentDesc;
    private String assessmentTime;
}
