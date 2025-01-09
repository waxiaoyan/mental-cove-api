package com.mental.cove.feign;

import com.mental.cove.data.WechatSessionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wechat", url = "${wechat.url}")
public interface WechatFeign {

    @GetMapping("/sns/jscode2session")
    WechatSessionResponse getSession(@RequestParam("appid") String appid,
                                     @RequestParam("secret") String secret,
                                     @RequestParam("js_code") String jsCode,
                                     @RequestParam(value = "grant_type", defaultValue = "authorization_code") String grantType);
}
