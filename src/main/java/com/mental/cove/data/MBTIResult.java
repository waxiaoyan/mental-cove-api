package com.mental.cove.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MBTIResult {
    private String userId;
    private String mbtiType;
    private String mbtiDescription;
}
