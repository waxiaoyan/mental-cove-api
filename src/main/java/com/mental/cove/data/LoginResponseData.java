package com.mental.cove.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseData {
    private String token;
    private String userId;
    private String userName;
}
