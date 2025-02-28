package com.mental.cove.data;

import lombok.*;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SdsResultExplanation extends ResultExplanation{
    private String result;
    private int score;
    private String explanation;
    private String suggestion;
}
