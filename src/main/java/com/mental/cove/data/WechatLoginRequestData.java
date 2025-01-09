package com.mental.cove.data;

import lombok.Data;

@Data
public class WechatLoginRequestData {
    private String code;
    private String encryptedData;
    private String iv;
}
