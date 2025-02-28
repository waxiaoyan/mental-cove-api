package com.mental.cove.data;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SdsResultResponse {
    private String result;
    private int score;
    private String explanation;
    private String suggestion;
}
