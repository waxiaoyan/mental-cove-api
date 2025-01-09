package com.mental.cove.service;

import com.mental.cove.data.WechatSessionResponse;
import com.mental.cove.feign.WechatFeign;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WechatService {

    @Value("${wechat.appid}")
    private String appId;

    @Value("${wechat.secret}")
    private String secret;

    @Resource
    WechatFeign wechatFeign;

    public WechatSessionResponse getOpenId(String code) {
        log.info("[WechatService|getOpenId] code is: {}", code);
        return wechatFeign.getSession(appId, secret, code, "authorization_code");
    }
}
