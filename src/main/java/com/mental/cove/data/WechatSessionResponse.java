package com.mental.cove.data;

import lombok.Data;

@Data
public class WechatSessionResponse {
    private String openid;
    private String session_key;
}
