package com.mental.cove.controller;

import com.mental.cove.data.LoginResponseData;
import com.mental.cove.data.WechatLoginRequestData;
import com.mental.cove.data.WechatSessionResponse;
import com.mental.cove.entity.User;
import com.mental.cove.service.JwtTokenProvider;
import com.mental.cove.service.UserService;
import com.mental.cove.service.WechatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Resource
    private WechatService wechatService;

    @Resource
    private UserService userService;

    @Resource
    private JwtTokenProvider jwtTokenProvider;


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody WechatLoginRequestData wechatLoginRequestData) {
        try {
            WechatSessionResponse wechatSessionResponse = wechatService.getOpenId(wechatLoginRequestData.getCode());
            String openid = wechatSessionResponse.getOpenid();
            User user = userService.getOrCreateUser(openid);
            String token = jwtTokenProvider.createToken(openid, wechatSessionResponse.getSession_key());
            LoginResponseData loginResponseData = LoginResponseData.builder()
                    .token(token)
                    .userId(String.valueOf(user.getId()))
                    .userName(user.getUserName() == null ? "微信用户" : user.getUserName())
                    .build();
            return ResponseEntity.ok().body(loginResponseData);
        } catch (Exception e) {
            log.error("Login failed for code:{}", wechatLoginRequestData.getCode(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to authenticate with WeChat");
        }

    }
}
